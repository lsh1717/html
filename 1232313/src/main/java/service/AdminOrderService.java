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

    private final AdminOrderMapper mapper;   // 관리자 전용 목록/필터/삭제
    private final OrderMapper orderMapper;   // 기등록된 findOrderItemsByOrderId 재사용

    public int count(String status, String keyword){
        return mapper.count(status, keyword);
    }

    public List<Orders> findPaged(int page, int size, String status, String keyword){
        int p = Math.max(1, page);
        int offset = (p - 1) * size;
        List<Orders> list = mapper.findPaged(offset, size, status, keyword);
        // 간단 요약(첫 아이템 제목 + 외 n개)
        for (Orders o : list) {
            String summary = mapper.summary(o.getOrderId());
            o.setStatus(o.getStatus()); // 직렬화 보장
            // summary를 Orders VO에 임시로 넣고 싶다면 lombok @Data 이용해 setSummary 추가(아래 VO 섹션 참고)
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
        // 허용값 검증
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