package service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
 * - 대시보드용 통계(오늘 주문 수/매출, 최근 주문/일/월매출, 상태 분포, 상태별 건수)
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /** 회원 PK 기준 주문(헤더+아이템) 조회 */
    public List<Orders> getOrdersByMemberId(Long memberId) {
        return orderMapper.findOrdersWithItems(memberId);
    }

    /** 주문 생성 트랜잭션 */
    @Transactional
    public void createOrder(Orders order, List<OrderItem> orderItems) {
        orderMapper.insertOrder(order); // SelectKey 로 orderId 세팅됨
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                item.setOrderId(order.getOrderId());
                orderMapper.insertOrderItem(item);
            }
        }
    }

    /** 특정 주문의 아이템 목록 조회(제목/표지 포함) */
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderMapper.findOrderItemsByOrderId(orderId);
    }

    // ===== 관리자 대시보드용 보조 메서드들 =====

    /** 오늘 주문 건수 */
    public int countTodayOrders() {
        return orderMapper.countTodayOrders();
    }

    /** 오늘 매출(취소 제외) */
    public int todayRevenue() {
        Integer v = orderMapper.sumTodayRevenue();
        return v == null ? 0 : v;
    }

    /** 동일 기능(호환용 메서드명) */
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
    public List<Map<String, Object>> monthlyRevenue(int months) {
        List<Map<String, Object>> list = orderMapper.monthlyRevenue(months);
        return list == null ? Collections.emptyList() : list;
    }

    /** 주문 상태 분포(PAID/SHIPPING/DELIVERED/CANCELLED) */
    public List<Map<String, Object>> statusCounts() {
        List<Map<String, Object>> list = orderMapper.statusCounts();
        return list == null ? Collections.emptyList() : list;
    }

    // ===== 새로 추가: 대시보드 정확도 향상 =====

    /** 상태별 주문 건수 (출고 대기=PAID, 배송 중=SHIPPING) */
    public int countByStatus(String status) {
        Integer v = orderMapper.countByStatus(status);
        return v == null ? 0 : v;
    }

    /**
     * 최근 N일 일자별 매출(취소 제외)
     * - Mapper 반환 예시: [{d: '2025-08-10', amount: 120000}, ...]
     *   또는 d/date(일자) + amount/sum/total 등의 키를 포함.
     */
    public List<Map<String, Object>> dailyRevenue(int days) {
        List<Map<String, Object>> list = orderMapper.dailyRevenue(days);
        return list == null ? Collections.emptyList() : list;
    }
}