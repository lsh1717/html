package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.AdminOrderMapper;
import vo.OrderItem;
import vo.Orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderMapper mapper;

    public Map<String,Object> list(int page, int size, String status, String keyword){
        int total = mapper.count(status, keyword);
        int totalPage = (int)Math.ceil(total / (double)size);
        int offset = (page-1) * size;
        List<Orders> rows = mapper.list(status, keyword, offset, size);

        Map<String,Object> resp = new HashMap<>();
        resp.put("page", page);
        resp.put("size", size);
        resp.put("totalCount", total);
        resp.put("totalPage", totalPage);
        resp.put("items", rows);
        return resp;
    }

    public Map<String,Object> detail(Long orderId){
        Orders hdr = mapper.findHeader(orderId);
        List<OrderItem> items = mapper.findItems(orderId);
        Map<String,Object> resp = new HashMap<>();
        resp.put("orderId", hdr.getOrderId());
        resp.put("userId", hdr.getUserId());
        resp.put("orderDate", hdr.getOrderDate());
        resp.put("totalAmount", hdr.getTotalAmount());
        resp.put("status", hdr.getStatus());
        resp.put("items", items);
        return resp;
    }

    public boolean updateStatus(Long orderId, String status){
        return mapper.updateStatus(orderId, status) > 0;
    }

    public int deleteIfPaid(Long orderId){
        mapper.deleteItems(orderId);
        return mapper.deleteOrderIfPaid(orderId); // 1이면 삭제 성공, 0이면 (PAID가 아니라) 실패
    }
}