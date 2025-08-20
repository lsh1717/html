package vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItem {
    private Long orderItemId;   // PK
    private Long orderId;       // 주문 ID (FK)
    private Long bookId;        // 도서 ID (FK)
    private int quantity;       // 수량
    private BigDecimal price;   // 주문 당시 단가 (BigDecimal)

    private Book book;    // 조인용 - 책 제목
}