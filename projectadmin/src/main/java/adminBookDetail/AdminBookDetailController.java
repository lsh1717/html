package adminBookDetail;

import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/detail")
public class AdminBookDetailController {

    @Autowired
    private AdminBookDetailService bookDetailService;

    @GetMapping("/{bookId}")
    public String showBookDetail(@PathVariable("bookId") int bookId, Model model) {
        Book book = bookDetailService.getBookDetail(bookId);
        model.addAttribute("book", book);
        return "adminBookDetail/book_detail";  // JSP 위치에 맞게 경로 조정
    }
}