package adminBookManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Book;

@Controller
//@RequestMapping("/admin/bookManage")
public class BookManageController {

    @Autowired
    private BookManageService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", service.getAll());
        return "list";
    }

    @GetMapping("/form/{bookId}")
    public String Form(@PathVariable Long bookId, Model model) {
        Book book = service.getById(bookId); // bookId로 도서 조회
        model.addAttribute("book", book);
        return "form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Book book) {
        service.save(book);
        return "redirect:/admin/bookManage";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", service.getById(id));
        return "form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/bookManage";
    }
}