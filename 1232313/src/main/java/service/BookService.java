package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vo.Book;
import repository.BookMapper;

@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    public List<Book> getAllBooks() {
        return bookMapper.findAll();
    }

    public List<Book> getPagedBooks(String keyword, int page, int pageSize) {
        int startRow = (page - 1) * pageSize;
        return bookMapper.findPageByKeyword(keyword, startRow, pageSize);
    }

    public int getTotalCount(String keyword) {
        return bookMapper.countByKeyword(keyword);
    }

    public Book getBookById(Long bookId) {
		return bookMapper.selectBookById(bookId);
	}
}