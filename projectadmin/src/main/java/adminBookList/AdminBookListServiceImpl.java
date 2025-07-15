package adminBookList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import domain.Book;

@Service
@Primary
public class AdminBookListServiceImpl implements AdminBookListService {

    @Autowired
    private AdminBookListRepository repository;

    @Override
    public void addBook(Book book) {
        repository.insertBook(book);
    }

    @Override
    public void updateBook(Book book) {
        repository.updateBook(book);
    }

    @Override
    public void deleteBook(int bookId) {
        repository.deleteBook(bookId);
    }

    @Override
    public Book getBookById(int bookId) {
        return repository.selectBookById(bookId);
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.selectAllBooks();
    }
}