package vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 주문 아이템 VO
 * - price: 주문 시점의 단가
 * - getLineTotal(): 단가×수량을 계산해 JSP에서 바로 ${it.lineTotal}로 사용
 * - book: 연관 도서(제목/표지 표시용)
 */
@Data
public class OrderItem {
    private Long orderItemId; // PK
    private Long orderId;     // 주문 ID(FK)
    private Long bookId;      // 도서 ID(FK)
    private int quantity;     // 수량
    private BigDecimal price; // 단가(주문 시점)
    private Book book;        // 연관 도서

    // (단가 × 수량) 계산 값 — DB 컬럼 없이도 JSP에서 바로 사용 가능
    public BigDecimal getLineTotal() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}