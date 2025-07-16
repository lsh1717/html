package adminBookManage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Book;

@Service
public class BookManageService {

    @Autowired
    private BookManageRepository repo;

    public List<Book> getAll() {
        return repo.findAll();
    }

    public void save(Book book) {
        repo.insert(book);
    }

    public Book getById(Long id) {
        return repo.findById(id);
    }

    public void update(Book book) {
        repo.update(book);
    }

    public void delete(Long id) {
        repo.delete(id);
    }
}