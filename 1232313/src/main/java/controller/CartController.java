package controller;

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
import javax.servlet.http.HttpSession;

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

    // JSON 직렬화/역직렬화를 위한 Jackson ObjectMapper
    private ObjectMapper mapper = new ObjectMapper();

    // ✅ 장바구니에 책 추가
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (quantity < 1) {
            quantity = 1;  // 수량이 0 이하인 경우 1로 설정
        }

        // 기존 장바구니 가져오기 (쿠키에서)
        List<CartItem> cart = getCartFromCookies(request);

        // 이미 장바구니에 있는 책인지 확인
        boolean found = false;
        for (CartItem item : cart) {
            if (Objects.equals(item.getBookId(), bookId)) {
                item.setQuantity(item.getQuantity() + quantity);  // 기존 수량에 더함
                found = true;
                break;
            }
        }

        // 새로 추가하는 책이면 장바구니에 추가
        if (!found) {
            cart.add(new CartItem(bookId, quantity));
        }

        // 수정된 장바구니를 다시 쿠키에 저장
        saveCartToCookie(response, cart);
        return "redirect:/cart/view";  // 장바구니 페이지로 이동
    }

    // ✅ 장바구니 보기
    @GetMapping("/cart/view")
    public String viewCart(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cart = getCartFromCookies(request);

        // 각 장바구니 항목에 책 정보(book 객체) 채워넣기
        for (CartItem item : cart) {
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) {
                item.setBook(book);
            }
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("pageTitle", "장바구니");
        model.addAttribute("bodyPage", "user/cart");  // layout.jsp에서 bodyPage include
        return "user/layout";  // layout 페이지로 이동
    }

    // ✅ 수량 변경
    @PostMapping("/cart/update")
    public String updateCartQuantity(
            @RequestParam Long bookId,
            @RequestParam int quantity,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<CartItem> cart = getCartFromCookies(request);

        if (quantity < 1) {
            // 수량이 1 미만이면 해당 항목 삭제
            cart.removeIf(item -> Objects.equals(item.getBookId(), bookId));
        } else {
            // 수량 업데이트
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

    // ✅ 장바구니 항목 삭제
    @PostMapping("/cart/remove")
    public String removeCartItem(
            @RequestParam Long bookId,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<CartItem> cart = getCartFromCookies(request);

        // Iterator 사용해서 안전하게 삭제
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

    // ✅ 쿠키에서 장바구니 읽어오기
    private List<CartItem> getCartFromCookies(HttpServletRequest request) throws Exception {
        List<CartItem> cart = new ArrayList<>();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    // URL 디코딩 후 JSON → 객체 변환
                    String cartJson = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                    cart = mapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
                }
            }
        }

        return cart;
    }

    // ✅ 장바구니 쿠키에 저장
    private void saveCartToCookie(HttpServletResponse response, List<CartItem> cart) throws Exception {
        // 객체 → JSON → URL 인코딩
        String cartJson = mapper.writeValueAsString(cart);
        String encodedCart = URLEncoder.encode(cartJson, StandardCharsets.UTF_8.name());
        
        Cookie cartCookie = new Cookie("cart", encodedCart);
        cartCookie.setPath("/");  // 모든 경로에서 접근 가능
        cartCookie.setMaxAge(7 * 24 * 60 * 60);  // 7일간 유지
        response.addCookie(cartCookie);
    }
    
    @GetMapping("/user/payment")
    public String showPaymentPage(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cartItems = getCartFromCookies(request);

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("error", "장바구니가 비어있습니다.");
            return "redirect:/user/bookList";
        }

        // 책 정보 채워넣기
        for (CartItem item : cartItems) {
            Book book = bookService.getBookById(item.getBookId());
            if (book != null) {
                item.setBook(book);
            }
        }

        // 총 합계 계산 (null 방지)
        int total = 0;
        for (CartItem item : cartItems) {
            if (item.getBook() != null) {
              total += item.getQuantity() * item.getBook().getPrice();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("pageTitle", "결제하기");
        model.addAttribute("bodyPage", "user/payment");
        return "user/layout";
    }
    
}