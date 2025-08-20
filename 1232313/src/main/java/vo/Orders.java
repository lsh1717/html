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
    private BigDecimal totalAmount;
    private String status;

    private List<OrderItem> items; // 주문 아이템 리스트
}