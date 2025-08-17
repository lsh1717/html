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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.BookService;
import vo.Book;
import vo.CartItem;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    private ObjectMapper mapper = new ObjectMapper();

    // 장바구니에 책 추가
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

    // 장바구니 보기
    @GetMapping("/cart/view")
    public String viewCart(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cart = getCartFromCookies(request);

        for (CartItem item : cart) {
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) item.setBook(book);
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("pageTitle", "장바구니");
        model.addAttribute("bodyPage", "user/cart");
        return "user/layout";
    }

    // 수량 변경
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

    // 항목 삭제
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

    // 결제 페이지
    @PostMapping("/user/payment")
    public String showPaymentPage(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cartItems = getCartFromCookies(request);

        if (cartItems.isEmpty()) return "redirect:/user/bookList";

        // 책 정보 채우기 & 총합 계산
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) {
                item.setBook(book);
                total = total.add(book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("pageTitle", "결제하기");
        model.addAttribute("bodyPage", "user/payment");
        return "user/layout";
    }

 // 결제 완료
    @PostMapping("/user/payment/complete")
    public String completePayment(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model) throws Exception {

        List<CartItem> cartItems = getCartFromCookies(request);
        if (cartItems.isEmpty()) return "redirect:/user/bookList";

        // DB 재고 차감
        for (CartItem item : cartItems) {
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) {
                int newStock = book.getStock().intValue() - item.getQuantity();
                book.setStock((long)newStock);
                bookService.updateBookStock(book);
            }
        }

        // 장바구니 쿠키 초기화
        Cookie cartCookie = new Cookie("cart", "");
        cartCookie.setPath("/");
        cartCookie.setMaxAge(0);
        response.addCookie(cartCookie);

        // layout.jsp에서 include할 JSP 파일 경로 설정
        model.addAttribute("pageTitle", "결제 완료");
        model.addAttribute("bodyPage", "/user/paymentComplete");  // ★ 여기를 절대경로로 바꿔서 include 문제 방지
        return "user/layout";
    }
    // --- 쿠키 읽기/쓰기 ---
    private List<CartItem> getCartFromCookies(HttpServletRequest request) throws Exception {
        List<CartItem> cart = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String cartJson = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                    cart = mapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
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