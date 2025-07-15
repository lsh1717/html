package adminBookList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 도서 목록 페이지 렌더링
    @GetMapping("/bookList")
    public String showBookList(Model model) {
        List<Book> books = adminService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("pageTitle", "도서 목록");
        return "book/bookList";  // JSP 경로에 맞게 조정 (혹은 admin/book_list)
    }

    // 도서 등록 폼 요청 (GET)
    @GetMapping("/add")
    public String showAddForm() {
        return "admin/admin_book_form";
    }

    // 도서 등록 처리 요청 (POST)
    @PostMapping("/add")
    public String addBook(Book book) {
        adminService.addBook(book);
        return "redirect:/admin/bookList";  // 경로 수정
    }

    // 도서 수정 폼 요청 (GET)
    @GetMapping("/update")
    public String showUpdateForm() {
        return "admin/admin_book_form";
    }

    // 도서 수정 처리 요청 (POST)
    @PostMapping("/update")
    public String updateBook(Book book) {
        adminService.updateBook(book);  // 수정용 메서드 호출
        return "redirect:/admin/bookList";
    }

    // 도서 삭제
    @GetMapping("/delete")
    public String deleteBook(int bookId) {
        adminService.deleteBook(bookId);  // 삭제 메서드 호출
        return "redirect:/admin/bookList";
    }
}