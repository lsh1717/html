package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.BookService;
import vo.Book;

@Controller
public class UserController {

    @Autowired
    private BookService bookService;

    @GetMapping("/user/bookList")
    public String bookList(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "keyword", required = false) String keyword,
        Model model
    ) {
        int pageSize = 8;

        // 전체 도서 수 조회 (검색어 포함)
        int totalCount = bookService.getTotalCount(keyword);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // 페이지 블록 계산
        int blockSize = 5;
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPage);

        // 해당 페이지 도서 목록 가져오기
        List<Book> books = bookService.getPagedBooks(keyword, page, pageSize);

        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageTitle", "도서 목록");
        model.addAttribute("bodyPage", "user/bookList"); // include용 경로
        return "user/layout"; // 메인 템플릿
    }
}