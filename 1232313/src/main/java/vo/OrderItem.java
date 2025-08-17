package vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long orderItemId;
    private Long orderId;
    private Long bookId;
    private int quantity;
    private BigDecimal price; // 주문 당시 단가

    // 책 정보 (optional)
    private Book book;
}