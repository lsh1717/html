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

    // 주문에 속한 아이템 리스트
    private List<OrderItem> items;
}