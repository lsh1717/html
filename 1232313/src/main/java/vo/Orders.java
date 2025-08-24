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
    private BigDecimal totalAmount;   // 총액
    private String status;            // PAID / SHIPPING / DELIVERED / CANCELLED

    /** 관리자 목록용: 품목 요약 (예: "불편한 편의점 4, 공정하다는 착각, …") */
    private String summary;           // ✅ MyBatis가 AS summary 로 내려주는 값을 받는 필드

    private List<OrderItem> items;    // 주문 아이템 목록
}