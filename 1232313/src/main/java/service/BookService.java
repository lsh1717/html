package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    private OrderMapper orderMapper; // 주문 처리용 Mapper 추가

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

    // ---------------------------
    // 결제 처리
    // ---------------------------
    public boolean processPayment(List<CartItem> cartItems) {
        // 1️⃣ 재고 확인 및 차감
        for (CartItem item : cartItems) {
            Book book = bookMapper.selectBookById(item.getBook().getBookId());

            if (book == null || book.getStock() < item.getQuantity()) {
                return false; // 재고 부족 시 결제 실패
            }

            book.setStock(book.getStock() - item.getQuantity());
            bookMapper.updateBookStock(book);
        }

        // 2️⃣ 주문 기록 생성
        Orders order = new Orders();
        order.setUserId(getCurrentMemberId()); // 로그인된 회원 ID
        orderMapper.insertOrder(order); // PK(orderId) 자동 생성

        // 3️⃣ 주문 상세(OrderItem) 기록 생성
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setBookId(item.getBook().getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getBook().getPrice()); // 주문 당시 가격
            orderMapper.insertOrderItem(orderItem);
        }

        return true; // 결제 성공
    }

    // ---------------------------
    // 재고 업데이트
    // ---------------------------
    public void updateBookStock(Book book) {
        bookMapper.updateBookStock(book);
    }

    // ---------------------------
    // 현재 로그인 회원 ID 가져오기 (구현 필요)
    // ---------------------------
    private Long getCurrentMemberId() {
        // 예시: SecurityContextHolder에서 회원 ID 가져오기
        return 1L; // 테스트용 임시값
    }
}