package controller;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.BookService;
import service.MemberService;
import service.OrderService;
import vo.Book;
import vo.CartItem;
import vo.Member;
import vo.OrderItem;
import vo.Orders;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;   // ✅ 대소문자 주의 (orderService)

    private final ObjectMapper mapper = new ObjectMapper();

    // ---------------- 장바구니 추가 ----------------
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam int quantity,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        if (quantity < 1) quantity = 1;

        List<CartItem> cart = getCartFromCookies(request);

        boolean found = false;
        for (CartItem item : cart) {
            if (Objects.equals(item.getBookId(), bookId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) cart.add(new CartItem(bookId, quantity));

        saveCartToCookie(response, cart);
        return "redirect:/cart/view";
    }

    // ---------------- 장바구니 보기 ----------------
    @GetMapping("/cart/view")
    public String viewCart(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cart = getCartFromCookies(request);

        for (CartItem item : cart) {
            if (item.getBookId() == null) continue;
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) item.setBook(book);
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("pageTitle", "장바구니");
        model.addAttribute("bodyPage", "user/cart");
        return "user/layout";
    }

    // ---------------- 수량 변경 ----------------
    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam Long bookId,
                                     @RequestParam int quantity,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        List<CartItem> cart = getCartFromCookies(request);

        if (quantity < 1) {
            cart.removeIf(item -> Objects.equals(item.getBookId(), bookId));
        } else {
            for (CartItem item : cart) {
                if (Objects.equals(item.getBookId(), bookId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }

        saveCartToCookie(response, cart);
        return "redirect:/cart/view";
    }

    // ---------------- 항목 삭제 ----------------
    @PostMapping("/cart/remove")
    public String removeCartItem(@RequestParam Long bookId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        List<CartItem> cart = getCartFromCookies(request);

        Iterator<CartItem> iterator = cart.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (Objects.equals(item.getBookId(), bookId)) {
                iterator.remove();
                break;
            }
        }

        saveCartToCookie(response, cart);
        return "redirect:/cart/view";
    }

    // ---------------- 결제 페이지 ----------------
    @PostMapping("/user/payment")
    public String showPaymentPage(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cartItems = getCartFromCookies(request);
        if (cartItems.isEmpty()) return "redirect:/user/bookList";

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            if (item.getBookId() == null) continue;
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) {
                item.setBook(book);
                BigDecimal price = book.getPrice() != null ? book.getPrice() : BigDecimal.ZERO;
                total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("pageTitle", "결제하기");
        model.addAttribute("bodyPage", "user/payment");
        return "user/layout";
    }

    // ---------------- 결제 완료 ----------------
    @PostMapping("/user/payment/complete")
    public String completePayment(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model) throws Exception {

        // 1) 장바구니 확인
        List<CartItem> cartItems = getCartFromCookies(request);
        if (cartItems.isEmpty()) return "redirect:/user/bookList";

        // 2) 로그인 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : null;
        if (username == null || "anonymousUser".equals(username)) {
            return "redirect:/login/login";
        }

        Member member = memberService.getMemberByLoginId(username);
        if (member == null || member.getUserId() == null) {
            throw new IllegalStateException("회원 정보(userId)가 존재하지 않습니다. username=" + username);
        }

        // 3) Orders & OrderItems 준비
        Orders order = new Orders();
        order.setUserId(member.getUserId().longValue());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO; // ★ 총액 누적

        for (CartItem cartItem : cartItems) {
            if (cartItem.getBookId() == null) {
                throw new IllegalStateException("장바구니에 bookId가 비어있는 항목이 있습니다.");
            }

            Book book = bookService.getBookById(cartItem.getBookId());
            if (book == null || book.getBookId() == null) {
                throw new IllegalStateException("존재하지 않는 도서입니다. bookId=" + cartItem.getBookId());
            }

            int stock = (book.getStock() == null) ? 0 : book.getStock().intValue();
            int qty   = Math.max(1, cartItem.getQuantity());
            if (stock < qty) {
                throw new IllegalStateException(
                    "재고가 부족합니다. bookId=" + book.getBookId() + ", 재고=" + stock + ", 요청수량=" + qty);
            }

            // 재고 차감
            int newStock = stock - qty;
            book.setStock((long) newStock);
            bookService.updateBookStock(book);

            // 가격 NULL 방지
            BigDecimal price = (book.getPrice() != null) ? book.getPrice() : BigDecimal.ZERO;

            // 총액 누적
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(qty)));

            // 주문 아이템 생성
            OrderItem item = new OrderItem();
            item.setBookId(book.getBookId());
            item.setQuantity(qty);
            item.setPrice(price);
            item.setBook(book);
            orderItems.add(item);
        }

        // ★ 총액/상태 세팅 (NULL 금지)
        order.setTotalAmount(totalAmount);
        order.setStatus("PAID");

        // 4) 저장 (OrderService에 @Transactional)
        orderService.createOrder(order, orderItems);  // ✅ 수정 완료

        // 5) 장바구니 쿠키 초기화
        Cookie cartCookie = new Cookie("cart", "");
        cartCookie.setPath("/");
        cartCookie.setMaxAge(0);
        response.addCookie(cartCookie);

        model.addAttribute("pageTitle", "결제 완료");
        model.addAttribute("bodyPage", "/user/paymentComplete");
        return "user/layout";
    }

    // ---------------- 쿠키 유틸 ----------------
    private List<CartItem> getCartFromCookies(HttpServletRequest request) throws Exception {
        List<CartItem> cart = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String cartJson = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                    if (cartJson != null && !cartJson.isEmpty()) {
                        cart = mapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
                    }
                }
            }
        }
        return cart;
    }

    private void saveCartToCookie(HttpServletResponse response, List<CartItem> cart) throws Exception {
        String cartJson = mapper.writeValueAsString(cart);
        String encodedCart = URLEncoder.encode(cartJson, StandardCharsets.UTF_8.name());
        Cookie cartCookie = new Cookie("cart", encodedCart);
        cartCookie.setPath("/");
        cartCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cartCookie);
    }
}