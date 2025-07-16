package bookList;

import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookListService {
    @Autowired
    private BookListRepository repo;

    public List<Book> getAll() {
        return repo.findAll();
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return repo.findByKeyword(keyword);
    }

    public int getTotalCount(String keyword) {
        return repo.countByKeyword(keyword);
    }

    public List<Book> getPagedBooks(String keyword, int page, int pageSize) {
        int startRow = (page - 1) * pageSize;
        int endRow = page * pageSize;
        return repo.findPageByKeyword(keyword, startRow, endRow);
    }
}

