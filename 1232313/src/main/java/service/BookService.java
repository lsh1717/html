package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vo.Book;
import vo.CartItem;
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

    public boolean processPayment(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Book book = bookMapper.selectBookById(item.getBook().getBookId());

            if (book == null || book.getStock() < item.getQuantity()) {
                return false; // 재고 부족 시 실패 처리
            }

            // 재고 차감
            book.setStock(book.getStock() - item.getQuantity());
            bookMapper.updateBookStock(book); // 재고 업데이트 쿼리 필요
        }

        // (선택) 주문 기록 저장도 여기서 처리 가능

        return true;
    }
}