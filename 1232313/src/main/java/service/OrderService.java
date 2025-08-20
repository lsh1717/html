package service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.OrderMapper;
import vo.OrderItem;
import vo.Orders;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public List<Orders> getOrdersByMemberId(Long memberId) {
        return orderMapper.findOrdersWithItems(memberId);
    }

    @Transactional
    public void createOrder(Orders order, List<OrderItem> orderItems) {
        orderMapper.insertOrder(order);
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
            orderMapper.insertOrderItem(item);
        }
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderMapper.findOrderItemsByOrderId(orderId);
    }

    // 📊 오늘 주문 수
    public int countTodayOrders() {
        return orderMapper.countTodayOrders();
    }

    // 📊 오늘 매출 합계
    public int todayRevenue() {
        return orderMapper.sumTodayRevenue();
    }

    // 📊 최근 주문 n개
    public List<Orders> findRecentOrders(int limit) {
        return orderMapper.findRecentOrders(limit);
    }
}