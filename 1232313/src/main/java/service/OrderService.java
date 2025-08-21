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

    // �윋� �삤�뒛 二쇰Ц �닔
    public int countTodayOrders() {
        return orderMapper.countTodayOrders();
    }

    // �윋� �삤�뒛 留ㅼ텧 �빀怨�
    public int todayRevenue() {
        return orderMapper.sumTodayRevenue();
    }

    // �윋� 理쒓렐 二쇰Ц n媛�
    public List<Orders> findRecentOrders(int limit) {
        return orderMapper.findRecentOrders(limit);
    }
    
    public int sumTodayRevenue(){ return orderMapper.sumTodayRevenue(); }
    public List<Orders> findRecent(int limit){ return orderMapper.findRecent(limit); }
    public List<java.util.Map<String,Object>> monthlyRevenue(int months){ return orderMapper.monthlyRevenue(months); }
    public List<java.util.Map<String,Object>> statusCounts(){ return orderMapper.statusCounts(); }
}