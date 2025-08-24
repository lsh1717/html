package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import service.BookService;
import service.MemberService;
import service.OrderService;
import service.ReviewService;
import vo.Book;
import vo.Member;
import vo.Orders;
import vo.Review;

/**
 * 사용자 전용 화면 컨트롤러
 * - 도서 목록/상세(+리뷰)
 * - 마이페이지(주문 내역)
 * - 리뷰 등록(구매자 + 미작성자만)
 */
@Controller
public class UserController {

    @Autowired private BookService bookService;
    @Autowired private MemberService memberService;
    @Autowired private OrderService orderService;
    @Autowired private ReviewService reviewService; // 리뷰 서비스

    /** 도서 목록 */
    @GetMapping("/user/bookList")
    public String bookList(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           Model model) {

        final int pageSize = 8;

        int totalCount = bookService.getTotalCount(keyword);
        int totalPage  = (int) Math.ceil((double) totalCount / pageSize);

        final int blockSize = 5;
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        List<Book> books = bookService.getPagedBooks(keyword, page, pageSize);

        // 추천 도서 3권(랜덤)
        List<Book> recommendedBooks = new ArrayList<>();
        List<Book> allBooks = bookService.getAllBooks();
        if (allBooks != null && !allBooks.isEmpty()) {
            Collections.shuffle(allBooks);
            for (int i = 0; i < Math.min(3, allBooks.size()); i++) {
                recommendedBooks.add(allBooks.get(i));
            }
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

    /**
     * 도서 상세 + 리뷰
     * rpage: 리뷰 페이지(1부터), rsize: 페이지당 리뷰 수(5)
     */
    @GetMapping("/user/bookDetail/{bookId}")
    public String bookDetail(@PathVariable("bookId") Long bookId,
                             @RequestParam(value = "rpage", defaultValue = "1") int rpage,
                             Model model) {

        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);

        // ---- 리뷰 페이징/평균/히스토그램 ----
        final int rsize = 5;
        int totalReviews = reviewService.countByBook(bookId);
        int rtotalPage   = (int) Math.ceil(totalReviews / (double) rsize);

        if (rpage < 1) rpage = 1;
        if (rtotalPage > 0 && rpage > rtotalPage) rpage = rtotalPage;

        List<Review> reviews = reviewService.listByBook(bookId, rpage, rsize);
        Double avg = reviewService.avgRating(bookId);
        double avgRating = (avg == null ? 0.0 : avg.doubleValue());

        // 히스토그램(서비스에 전용 메서드가 없다면 간단 계산)
        int[] ratingCounts = new int[5];
        try {
            // 전부 긁어와서 카운팅(데이터가 많지 않다는 가정)
            List<Review> all = reviewService.listByBook(bookId, 1, 1000);
            for (Review r : all) {
                if (r.getRating() != null && r.getRating() >= 1 && r.getRating() <= 5) {
                    ratingCounts[r.getRating() - 1]++;
                }
            }
        } catch (Exception ignore) {
            // 실패 시 0으로 둠
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("ratingCounts", ratingCounts);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("rpage", rpage);
        model.addAttribute("rtotalPage", Math.max(rtotalPage, 1));

        // ---- 구매자만 리뷰 작성 가능(+이미 작성자는 불가) ----
        boolean canReview = false;
        Long loginUserId = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String loginId = auth.getName();
            Member me = memberService.getMemberByLoginId(loginId);
            if (me != null && me.getUserId() != null) {
                loginUserId = me.getUserId().longValue();
                boolean purchased = reviewService.hasPurchased(loginUserId, bookId);
                boolean already   = reviewService.alreadyReviewed(loginUserId, bookId);
                canReview = purchased && !already;
            }
        }

        model.addAttribute("canReview",  canReview);
        model.addAttribute("loginUserId", loginUserId);

        model.addAttribute("pageTitle", "도서 상세");
        model.addAttribute("bodyPage", "user/bookDetail");
        return "user/layout";
    }

    /** 리뷰 등록 (구매자 + 미작성자만) */
    @PostMapping("/reviews")
    public String createReview(@RequestParam Long bookId,
                               @RequestParam int rating,
                               @RequestParam String content,
                               HttpServletRequest req,
                               RedirectAttributes ra) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            ra.addFlashAttribute("msg", "로그인 후 작성할 수 있습니다.");
            return "redirect:" + req.getContextPath() + "/user/bookDetail/" + bookId;
        }

        Member me = memberService.getMemberByLoginId(auth.getName());
        if (me == null || me.getUserId() == null) {
            ra.addFlashAttribute("msg", "회원 정보를 찾을 수 없습니다.");
            return "redirect:" + req.getContextPath() + "/user/bookDetail/" + bookId;
        }
        long userId = me.getUserId().longValue();

        if (!reviewService.hasPurchased(userId, bookId)) {
            ra.addFlashAttribute("msg", "구매한 이용자만 리뷰를 작성할 수 있습니다.");
            return "redirect:" + req.getContextPath() + "/user/bookDetail/" + bookId;
        }
        if (reviewService.alreadyReviewed(userId, bookId)) {
            ra.addFlashAttribute("msg", "이미 리뷰를 작성하셨습니다.");
            return "redirect:" + req.getContextPath() + "/user/bookDetail/" + bookId;
        }

        Review r = new Review();
        r.setBookId(bookId);
        r.setUserId(userId);
        r.setRating(Math.max(1, Math.min(5, rating)));
        r.setContent(content);
        reviewService.add(r);

        ra.addFlashAttribute("msg", "리뷰가 등록되었습니다.");
        return "redirect:" + req.getContextPath() + "/user/bookDetail/" + bookId + "?rpage=1#reviews";
    }

    /** 마이페이지: 주문 내역 */
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