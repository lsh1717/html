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

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private OrderMapper orderMapper; // 주문 처리용 Mapper

    // ---------------------------
    // 도서 조회 관련
    // ---------------------------
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

    // 전체 도서 개수 (대시보드용)
    public int countBooks() {
        return bookMapper.countAll();
    }

    // ---------------------------
    // 결제 처리
    // ---------------------------
    public boolean processPayment(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Book book = bookMapper.selectBookById(item.getBook().getBookId());

            if (book == null || book.getStock() < item.getQuantity()) {
                return false;
            }

            book.setStock(book.getStock() - item.getQuantity());
            bookMapper.updateBookStock(book);
        }

        Orders order = new Orders();
        order.setUserId(getCurrentMemberId());
        orderMapper.insertOrder(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setBookId(item.getBook().getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getBook().getPrice());
            orderMapper.insertOrderItem(orderItem);
        }

        return true;
    }

    public void updateBookStock(Book book) {
        bookMapper.updateBookStock(book);
    }

    private Long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return 1L; // TODO: MemberMapper로 username → userId 변환 필요
        }
        return null;
    }
}