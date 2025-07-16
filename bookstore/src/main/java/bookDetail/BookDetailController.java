package bookDetail;

import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
// ★ 클래스 레벨 @RequestMapping 은 제거 ★
public class BookDetailController {

    @Autowired
    private BookDetailService service;

    /** 상세 페이지: /bookDetail/{id} */
    @GetMapping("/{id}")           
    public String detail(@PathVariable Long id, Model model) {
        Book book = service.getById(id);
        model.addAttribute("book", book);
        return "bookDetail";        // bookdetail-servlet.xml 의 prefix/postfix 에 맞춤
    }

    /** 커버 이미지: /bookDetail/{id}/cover */
    @GetMapping("/{id}/cover")      
    public void coverImage(@PathVariable Long id,
                           HttpServletResponse resp) throws IOException {
        byte[] img = service.getCoverImage(id);
        resp.setContentType("image/jpeg");
        resp.getOutputStream().write(img);
    }
}
