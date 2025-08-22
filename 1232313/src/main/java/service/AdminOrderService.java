package service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import repository.AdminOrderMapper;
import repository.OrderMapper;
import vo.OrderItem;
import vo.Orders;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderMapper mapper;   // ������ ���� ���/����/����
    private final OrderMapper orderMapper;   // ���ϵ� findOrderItemsByOrderId ����

    public int count(String status, String keyword){
        return mapper.count(status, keyword);
    }

    public List<Orders> findPaged(int page, int size, String status, String keyword){
        int p = Math.max(1, page);
        int offset = (p - 1) * size;
        List<Orders> list = mapper.findPaged(offset, size, status, keyword);
        // ���� ���(ù ������ ���� + �� n��)
        for (Orders o : list) {
            String summary = mapper.summary(o.getOrderId());
            o.setStatus(o.getStatus()); // ����ȭ ����
            // summary�� Orders VO�� �ӽ÷� �ְ� �ʹٸ� lombok @Data �̿��� setSummary �߰�(�Ʒ� VO ���� ����)
            try { Orders.class.getDeclaredField("summary"); } catch (NoSuchFieldException ignore) {}
        }
        return list;
    }

    public Orders findOneWithItems(Long orderId){
        Orders o = mapper.findOne(orderId);
        if (o == null) return null;
        List<OrderItem> items = orderMapper.findOrderItemsByOrderId(orderId);
        o.setItems(items);
        return o;
    }

    @Transactional
    public void updateStatus(Long orderId, String status){
        // ��밪 ����
        if (!"PAID".equals(status) && !"SHIPPING".equals(status) && !"DELIVERED".equals(status) && !"CANCELLED".equals(status)) {
            throw new IllegalArgumentException("invalid status");
        }
        mapper.updateStatus(orderId, status);
    }

    @Transactional
    public boolean deleteIfPaid(Long orderId){
        String cur = mapper.getStatus(orderId);
        if (!"PAID".equals(cur)) return false;
        mapper.deleteItems(orderId);
        mapper.deleteOrderIfPaid(orderId);
        return true;
    }
}