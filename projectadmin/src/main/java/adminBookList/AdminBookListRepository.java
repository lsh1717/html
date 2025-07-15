package adminBookList;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import domain.Book;

@Mapper
public interface AdminBookListRepository {
    void insertBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book selectBookById(int bookId);
    List<Book> selectAllBooks();
}