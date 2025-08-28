package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import repository.BookMapper;
import repository.OrderMapper;
import vo.Book;
import vo.CartItem;
import vo.OrderItem;
import vo.Orders;

@Service
public class BookService {

    @Autowired private BookMapper bookMapper;
    @Autowired private OrderMapper orderMapper; // 주문 처리용

    /* -------- 조회 -------- */

    public List<Book> getAllBooks() {
        return bookMapper.findAll();
    }

    /** 키워드 + 페이지네이션 (Oracle ROWNUM 방식) */
    public List<Book> getPagedBooks(String keyword, int page, int pageSize) {
        if (page < 1) page = 1;
        int startRow = (page - 1) * pageSize;   // 0, 8, 16, ...
        int endRow   = page * pageSize;         // 8, 16, 24, ...
        return bookMapper.findPageByKeyword(keyword, startRow, endRow);
    }

    public int getTotalCount(String keyword) {
        return bookMapper.countByKeyword(keyword);
    }

    public Book getBookById(Long bookId) {
        return bookMapper.selectBookById(bookId);
    }

    /** KPI: 전체 도서 수 */
    public int countBooks() {
        return bookMapper.countAll();
    }

    /* -------- 결제(예시) -------- */

    public boolean processPayment(List<CartItem> cartItems) {
        // 1) 재고 차감
        for (CartItem item : cartItems) {
            Book book = bookMapper.selectBookById(item.getBook().getBookId());
            if (book == null || book.getStock() < item.getQuantity()) return false;
            book.setStock(book.getStock() - item.getQuantity());
            bookMapper.updateBookStock(book);
        }
        // 2) 주문/아이템 기록
        Orders order = new Orders();
        order.setUserId(getCurrentMemberId());
        orderMapper.insertOrder(order);

        for (CartItem item : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getOrderId());
            oi.setBookId(item.getBook().getBookId());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getBook().getPrice());
            orderMapper.insertOrderItem(oi);
        }
        return true;
    }

    public void updateBookStock(Book book) {
        bookMapper.updateBookStock(book);
    }

    private Long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // TODO: 실제 로그인 사용자 PK 조회로 교체
            return 1L;
        }
        return null;
    }

    /* -------- 대시보드 보조 -------- */

    public List<Book> findLowStockBooks(int threshold, int limit) {
        return bookMapper.findLowStock(threshold, limit);
    }

    public List<Book> bestSellers(int days, int limit) {
        return bookMapper.findBestSellers(days, limit);
    }
    public List<Book> recommendedBooks(int days, int limit) {
        List<Book> top = bookMapper.findBestSellers(days, limit);
        if (top.size() < limit) {
            List<Long> exclude = top.stream().map(Book::getBookId).toList();
            top.addAll(bookMapper.pickRandomActive(exclude, limit - top.size()));
        }
        return top;
    }
    
}