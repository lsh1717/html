package repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import vo.Book;
//더미데이터 
@Repository
public class BookRepository {
    private static final List<Book> books = List.of(
        new Book(1, "스프링 입문", 15000),
        new Book(2, "자바의 정석", 27000),
        new Book(3, "토비의 스프링", 35000)
    );

    public Book findById(int id) {
        return books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Book> findAll() {
        return books;
    }
}