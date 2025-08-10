package controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import repository.BookRepository;
import vo.Book;
import vo.CartItem;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final BookRepository bookRepository;  // 책 정보를 가져오기 위한 저장소 객체
    private final ObjectMapper objectMapper = new ObjectMapper();  // JSON 변환 도구 (객체 ⇄ JSON)

    // 📘 메인 화면: 도서 목록 보여주기
    @GetMapping({"/", "/main"})
    public String main(Model model) {
        // bookRepository에서 모든 책 리스트 가져와서 뷰에 전달
        model.addAttribute("bookList", bookRepository.findAll());
        return "main";  // main.jsp 혹은 main.html 보여줌
    }

    // 🛒 장바구니 페이지 보여주기
    @GetMapping("/cart")
    public String viewCart(HttpServletRequest request, Model model) throws Exception {
        // 쿠키에서 저장된 장바구니 데이터를 꺼내서 리스트로 변환
        List<CartItem> cartList = getCartFromCookies(request.getCookies());
        model.addAttribute("cartList", cartList);  // 뷰에 장바구니 목록 전달
        return "cart";  // cart.jsp 혹은 cart.html 보여줌
    }

    // ➕ 장바구니에 책 추가하기 (책 id 받아서 수량 1 증가 또는 새로 추가)
    @GetMapping("/addCart")
    public String addToCart(int bookId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 현재 쿠키에 저장된 장바구니 가져오기
        List<CartItem> cartList = getCartFromCookies(request.getCookies());
        // 책 저장소에서 bookId로 책 정보 가져오기
        Book book = bookRepository.findById(bookId);

        if (book != null) {  // 책이 존재하면
            boolean found = false;
            // 이미 장바구니에 같은 책이 있는지 확인
            for (CartItem item : cartList) {
                if (item.getBook().getId() == bookId) {
                    // 있으면 수량 1 증가
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // 없으면 새로 추가 (수량 1)
                cartList.add(new CartItem(book, 1));
            }
        }

        // 변경된 장바구니를 쿠키에 다시 저장
        saveCartToCookies(cartList, response);
        // 장바구니 페이지로 리다이렉트
        return "redirect:/cart";
    }

    // ➖ 장바구니에 담긴 책 수량 줄이기
    @GetMapping("/decreaseCart")
    public String decreaseCart(int bookId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 쿠키에서 장바구니 데이터 불러오기
        List<CartItem> cartList = getCartFromCookies(request.getCookies());

        // 장바구니 목록 돌면서 해당 책 찾기
        for (Iterator<CartItem> it = cartList.iterator(); it.hasNext();) {
            CartItem item = it.next();
            if (item.getBook().getId() == bookId) {
                if (item.getQuantity() > 1) {
                    // 수량이 1 초과면 1 줄이기
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    // 수량이 1이면 제거 (0 되면 삭제)
                    it.remove();
                }
                break;
            }
        }

        // 변경된 장바구니 쿠키에 저장
        saveCartToCookies(cartList, response);
        // 다시 장바구니 페이지로 이동
        return "redirect:/cart";
    }

    // ❌ 장바구니에서 특정 책 완전 삭제하기
    @GetMapping("/removeCart")
    public String removeCart(int bookId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 쿠키에서 장바구니 목록 불러오기
        List<CartItem> cartList = getCartFromCookies(request.getCookies());
        // bookId가 같은 아이템 전부 삭제
        cartList.removeIf(item -> item.getBook().getId() == bookId);
        // 변경된 장바구니 쿠키에 저장
        saveCartToCookies(cartList, response);
        // 장바구니 페이지로 이동
        return "redirect:/cart";
    }

    // 🔄 쿠키에서 JSON 형태로 저장된 장바구니 데이터를 가져와서 List<CartItem> 객체로 변환
    private List<CartItem> getCartFromCookies(Cookie[] cookies) throws Exception {
        List<CartItem> cartList = new ArrayList<>();  // 빈 리스트 준비
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("cart".equals(c.getName())) {  // 이름이 'cart'인 쿠키 찾기
                    // 쿠키 값은 URL 인코딩 되어 있으므로 디코딩해서 원래 JSON 문자열로 만들기
                    String json = URLDecoder.decode(c.getValue(), "UTF-8");
                    // JSON을 CartItem 배열로 변환
                    CartItem[] items = objectMapper.readValue(json, CartItem[].class);
                    // 배열을 리스트로 변환 후 저장
                    cartList = new ArrayList<>(Arrays.asList(items));
                }
            }
        }
        return cartList;
    }

    // 💾 장바구니 데이터를 JSON 문자열로 변환해 쿠키에 저장
    private void saveCartToCookies(List<CartItem> cartList, HttpServletResponse response) throws Exception {
        // List<CartItem>를 JSON 문자열로 변환
        String json = objectMapper.writeValueAsString(cartList);
        // JSON 문자열을 URL 인코딩해서 쿠키에 저장 가능하게 만듦
        Cookie cookie = new Cookie("cart", URLEncoder.encode(json, "UTF-8"));
        cookie.setMaxAge(60 * 60); // 쿠키 만료 시간: 1시간
        response.addCookie(cookie);  // 응답에 쿠키 추가
    }
}