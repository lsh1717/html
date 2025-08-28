package service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import repository.AdminBookMapper;
import vo.Book;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminBookService {

    private final AdminBookMapper mapper;

    public int count(String keyword) {
        return mapper.count(keyword);
    }

    public List<Book> findPaged(String keyword, int page, int pageSize) {
        int p = Math.max(page, 1);
        int start = (p - 1) * pageSize + 1;
        int end   = p * pageSize;
        return mapper.findPaged(keyword, start, end);
    }

    public Book findById(Long id) {
        return mapper.findById(id);
    }

    public void create(Book b) {
        mapper.insert(b);
    }

    public void update(Book b) {
        mapper.update(b);
    }

    public void delete(Long id) {
        mapper.delete(id);
    }
    
    public List<Map<String,Object>> topBooks(int days, int limit){
        return mapper.topBooksByQty(days, limit);
    }
    
}