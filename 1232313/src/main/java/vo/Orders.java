package vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Orders {
    private Long orderId;
    private Long userId;
    private Date orderDate;
    private BigDecimal totalAmount; // 주문 총액
    private String status;          // 배송상태 PAID / SHIPPING / DELIVERED / CANCELLED

    private List<OrderItem> items;  // 주문 아이템 리스트
}