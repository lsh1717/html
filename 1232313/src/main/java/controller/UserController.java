package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import service.BookService;
import service.MemberService;
import service.OrderService;
import vo.Book;
import vo.Member;
import vo.Orders;

/**
 * 사용자 전용 화면 컨트롤러
 * - 도서 목록/상세
 * - 마이페이지(주문 내역)
 */
@Controller
public class UserController {

    @Autowired private BookService bookService;     // 도서 조회 서비스
    @Autowired private MemberService memberService; // 회원 조회 서비스
    @Autowired private OrderService orderService;   // 주문 조회/생성 서비스

    /**
     * 사용자 도서 목록 화면
     * - 검색/페이징 처리
     * - 추천 도서(랜덤 3권) 노출
     */
    @GetMapping("/user/bookList")
    public String bookList(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "keyword", required = false) String keyword,
        Model model
    ) {
        final int pageSize = 8; // 한 페이지당 8권

        int totalCount = bookService.getTotalCount(keyword);                 // 검색어 기준 총 개수
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);     // 총 페이지 수

        // 페이지 블럭(1~5, 6~10 …)
        final int blockSize = 5;
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        List<Book> books = bookService.getPagedBooks(keyword, page, pageSize);

        // 추천 도서: 전체에서 랜덤 3권 뽑기(간단 버전)
        List<Book> recommendedBooks = new ArrayList<>();
        List<Book> allBooks = bookService.getAllBooks();
        if (allBooks != null && !allBooks.isEmpty()) {
            Collections.shuffle(allBooks);
            for (int i = 0; i < Math.min(3, allBooks.size()); i++) {
                recommendedBooks.add(allBooks.get(i));
            }
        }

        // 뷰 모델 바인딩
        model.addAttribute("books", books);
        model.addAttribute("recommendedBooks", recommendedBooks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 공통 레이아웃에 내려줄 메타
        model.addAttribute("pageTitle", "도서 목록");
        model.addAttribute("bodyPage", "user/bookList"); // layout 안에서 include 되는 JSP 경로

        return "user/layout"; // 공통 레이아웃 JSP
    }

    /**
     * 도서 상세 화면
     */
    @GetMapping("/user/bookDetail/{bookId}")
    public String bookDetail(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", "도서 상세");
        model.addAttribute("bodyPage", "user/bookDetail");
        return "user/layout";
    }

    /**
     * 마이페이지
     * - 현재 로그인 사용자의 주문 내역을 불러와서 화면에 노출
     * - 주문상태는 사용하지 않고, 배송상태(status) 4단계만 사용(PAID/SHIPPING/DELIVERED/CANCELLED)
     */
    @GetMapping("/user/mypage")
    public String mypage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // 로그인 ID

        Member member = memberService.getMemberByLoginId(username);
        if (member == null || member.getUserId() == null) {
            throw new IllegalStateException("회원 PK(userId)를 가져오지 못했습니다. username=" + username);
        }

        // 주문 + 아이템 목록
        List<Orders> orders = orderService.getOrdersByMemberId(member.getUserId().longValue());

        // 바인딩
        model.addAttribute("member", member);
        model.addAttribute("orders", orders);
        model.addAttribute("pageTitle", "마이페이지");
        model.addAttribute("bodyPage", "user/mypage");

        return "user/layout";
    }
}