package adminBookList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import domain.Book;

@Controller
@RequestMapping("/admin")
public class AdminBookListController {

    @Autowired
    private AdminBookListService bookService;

    @GetMapping("/bookList")
    public String showBookList(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "adminBookList/bookList";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "adminBookList/admin_book_form";  // JSP 파일명에 맞게
    }

    @PostMapping("/add")
    public String addBook(Book book) {
        bookService.addBook(book);
        return "redirect:/admin/bookList";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("bookId") int bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        return "adminBookList/admin_book_form";
    }

    @PostMapping("/update")
    public String updateBook(Book book) {
        bookService.updateBook(book);
        return "redirect:/admin/bookList";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("bookId") int bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/admin/bookList";
    }
}