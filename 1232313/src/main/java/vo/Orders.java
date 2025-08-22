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
    private BigDecimal totalAmount; // �ֹ� �Ѿ�
    private String status;          // ��ۻ��� PAID / SHIPPING / DELIVERED / CANCELLED

    private List<OrderItem> items;  // �ֹ� ������ ����Ʈ
}