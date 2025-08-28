package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    
    @GetMapping("/top")
    @ResponseBody
    public Map<String,Object> topBooks(
            @RequestParam(defaultValue="30") int days,
            @RequestParam(defaultValue="5")  int limit) {

        List<Map<String,Object>> rows = service.topBooks(days, limit);

        List<String> labels = new ArrayList<>();
        List<Integer> units = new ArrayList<>();
        List<Long> revenue  = new ArrayList<>();

        for (Map<String,Object> r : rows) {
            // Map 키는 별칭과 동일하게 들어오지만 DB에 따라 대문자일 수 있어 이중으로 처리
            String title = (String) (r.get("title") != null ? r.get("title") : r.get("TITLE"));
            Number q     = (Number) (r.get("qty")    != null ? r.get("qty")    : r.get("QTY"));
            Number rev   = (Number) (r.get("revenue")!= null ? r.get("revenue"): r.get("REVENUE"));

            labels.add(title);
            units.add(q == null ? 0 : q.intValue());
            revenue.add(rev == null ? 0L : rev.longValue());
        }

        Map<String,Object> res = new HashMap<>();
        res.put("labels", labels);
        res.put("units",  units);
        res.put("revenue",revenue);
        return res;
    }
    
}