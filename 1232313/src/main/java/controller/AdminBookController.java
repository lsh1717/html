package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import service.AdminBookService;
import vo.Book;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/books")
public class AdminBookController {

    private final AdminBookService service;

    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String keyword){
        int total = service.count(keyword);
        int totalPage = (int)Math.ceil((double)total / size);
        List<Book> items = service.findPaged(keyword, page, size);
        Map<String,Object> res = new HashMap<>();
        res.put("page", page);
        res.put("totalPage", Math.max(totalPage, 1));
        res.put("total", total);
        res.put("items", items);
        return res;
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id){ return service.findById(id); }

    @PostMapping
    public void create(@RequestBody Book b){ service.create(b); }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Book b){
        b.setBookId(id);
        service.update(b);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }
}