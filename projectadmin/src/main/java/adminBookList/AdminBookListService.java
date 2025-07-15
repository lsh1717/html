package adminBookList;

import java.util.List;

import domain.Book;

public interface AdminBookListService {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
}