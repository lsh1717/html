package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.OrderMapper;
import vo.OrderItem;
import vo.Orders;

/**
 * 주문 관련 비즈니스 로직 서비스
 * - 조회(회원별 주문/아이템)
 * - 생성(주문 + 아이템 배치)
 * - 대시보드용 통계(오늘 주문 수/매출, 최근 주문/월매출/상태 분포)
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 회원 PK 기준 주문(헤더+아이템) 조회
     */
    public List<Orders> getOrdersByMemberId(Long memberId) {
        return orderMapper.findOrdersWithItems(memberId);
    }

    /**
     * 주문 생성 트랜잭션
     * 1) 주문 헤더 INSERT
     * 2) 각 아이템 INSERT(생성된 orderId 매핑)
     */
    @Transactional
    public void createOrder(Orders order, List<OrderItem> orderItems) {
        orderMapper.insertOrder(order); // orderId 생성됨(SelectKey)
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
            orderMapper.insertOrderItem(item);
        }
    }

    /**
     * 특정 주문의 아이템 목록 조회(제목/표지 포함)
     */
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderMapper.findOrderItemsByOrderId(orderId);
    }

    // ===== 관리자 대시보드용 보조 메서드들 =====

    /** 오늘 주문 건수 */
    public int countTodayOrders() {
        return orderMapper.countTodayOrders();
    }

    /**
     * 오늘 매출(취소 제외)
     * - Mapper는 NULL 가능성이 있으므로 0으로 안전 변환
     */
    public int todayRevenue() {
        Integer v = orderMapper.sumTodayRevenue();
        return v == null ? 0 : v;
    }

    /** (동일 기능, 메서드명만 다른 버전 — 호환용) */
    public int sumTodayRevenue() {
        Integer v = orderMapper.sumTodayRevenue();
        return v == null ? 0 : v;
    }

    /** 최근 주문 n개(간단 헤더용) */
    public List<Orders> findRecentOrders(int limit) {
        return orderMapper.findRecentOrders(limit);
    }

    /** 최근 주문 n개(대시보드 표용) */
    public List<Orders> findRecent(int limit) {
        return orderMapper.findRecent(limit);
    }

    /** 최근 N개월 월별 매출(취소 제외) */
    public List<java.util.Map<String, Object>> monthlyRevenue(int months) {
        return orderMapper.monthlyRevenue(months);
    }

    /**
     * 주문 상태 분포
     * - 상태는 4단계만 사용: PAID(결제 완료), SHIPPING(배송중), DELIVERED(배송완료), CANCELLED(취소)
     */
    public List<java.util.Map<String, Object>> statusCounts() {
        return orderMapper.statusCounts();
    }
}