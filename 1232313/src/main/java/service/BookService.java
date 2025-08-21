package service;

import java.util.List;
import java.util.Map;
import java.util.Collections;

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
    @Autowired private OrderMapper orderMapper; // 주문 처리용 Mapper

    // ---------------------------
    // 도서 조회
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

    // ✅ KPI: 전체 도서 수 (중복 제거 후 이 메서드만 유지)
    public int countBooks() { 
        return bookMapper.countAll(); 
    }

    // ---------------------------
    // 결제 처리
    // ---------------------------
    public boolean processPayment(List<CartItem> cartItems) {
        // 1) 재고 차감
        for (CartItem item : cartItems) {
            Book book = bookMapper.selectBookById(item.getBook().getBookId());
            if (book == null || book.getStock() < item.getQuantity()) return false;
            book.setStock(book.getStock() - item.getQuantity());
            bookMapper.updateBookStock(book);
        }
        // 2) 주문/주문아이템 기록
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
            // TODO: MemberMapper로 username -> userId 변환
            return 1L;
        }
        return null;
    }

    // ---------------------------
    // 대시보드 보조 데이터
    // ---------------------------
    public List<Book> findLowStockBooks(int threshold, int limit){
        return bookMapper.findLowStock(threshold, limit);
    }

    // (선택) 카테고리 분포 — Map 리스트로 반환
    public List<Map<String,Object>> categoryCounts(){
        try { return bookMapper.categoryCounts(); }
        catch (Exception e) { return Collections.emptyList(); }
    }
}