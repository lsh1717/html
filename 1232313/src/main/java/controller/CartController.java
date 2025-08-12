package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.BookService;
import vo.Book;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    private ObjectMapper mapper = new ObjectMapper();

    // 장바구니에 책 추가
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<CartItem> cart = new ArrayList<>();

        // 기존 쿠키에서 장바구니 정보 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String cartJson = cookie.getValue();
                    cart = mapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
                }
            }
        }

        // 동일 책이 있으면 수량 증가, 없으면 새로 추가
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(bookId, quantity));
        }

        // 쿠키에 JSON 형태로 저장
        String updatedCartJson = mapper.writeValueAsString(cart);
        Cookie cartCookie = new Cookie("cart", updatedCartJson);
        cartCookie.setPath("/");
        cartCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유지
        response.addCookie(cartCookie);

        return "redirect:/cart/view";
    }

    // 장바구니 화면 조회
    @GetMapping("/cart/view")
    public String viewCart(HttpServletRequest request, Model model) throws Exception {
        List<CartItem> cart = new ArrayList<>();

        // 쿠키에서 장바구니 정보 읽기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String cartJson = cookie.getValue();
                    cart = mapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
                }
            }
        }

        // bookId 목록 추출
        List<Long> bookIds = cart.stream()
                .map(CartItem::getBookId)
                .collect(Collectors.toList());

        // 책 정보 조회
        List<Book> books = new ArrayList<>();
        for (Long id : bookIds) {
            Book book = bookService.getBookById(id);
            if (book != null) books.add(book);
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("books", books);

        return "user/cart";  // user/cart.jsp
    }

    // 내부 CartItem 클래스 (필요시 분리해도 됨)
    public static class CartItem {
        private Long bookId;
        private int quantity;

        public CartItem() {}

        public CartItem(Long bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}