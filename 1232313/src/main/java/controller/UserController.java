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

@Controller
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    // 📚 도서 목록
    @GetMapping("/user/bookList")
    public String bookList(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "keyword", required = false) String keyword,
        Model model
    ) {
        int pageSize = 8;

        int totalCount = bookService.getTotalCount(keyword);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        List<Book> books = bookService.getPagedBooks(keyword, page, pageSize);

        // 추천 도서
        List<Book> recommendedBooks = new ArrayList<>();
        List<Book> allBooks = bookService.getAllBooks();
        Collections.shuffle(allBooks);
        for (int i = 0; i < Math.min(3, allBooks.size()); i++) {
            recommendedBooks.add(allBooks.get(i));
        }

        model.addAttribute("books", books);
        model.addAttribute("recommendedBooks", recommendedBooks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageTitle", "도서 목록");
        model.addAttribute("bodyPage", "user/bookList");
        return "user/layout";
    }

    // 📖 도서 상세
    @GetMapping("/user/bookDetail/{bookId}")
    public String bookDetail(@PathVariable("bookId") Long bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", "도서 상세");
        model.addAttribute("bodyPage", "user/bookDetail");
        return "user/layout";
    }

    // 👤 마이페이지
    @GetMapping("/user/mypage")
    public String mypage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Member member = memberService.getMemberByLoginId(username);
        if (member == null || member.getUserId() == null) {
            throw new IllegalStateException("회원 PK(userId)를 가져오지 못했습니다. username=" + username);
        }

        List<Orders> orders = orderService.getOrdersByMemberId(member.getUserId().longValue());

        model.addAttribute("member", member);
        model.addAttribute("orders", orders);
        model.addAttribute("pageTitle", "마이페이지");
        model.addAttribute("bodyPage", "user/mypage");

        return "user/layout";
    }
}