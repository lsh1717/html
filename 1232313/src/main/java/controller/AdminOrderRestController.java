package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.AdminOrderService;
import vo.Orders;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/orders")
public class AdminOrderRestController {

    private final AdminOrderService svc;

    // 목록 (검색/필터/페이징)
    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String status,
                                   @RequestParam(required=false) String keyword){
        int total = svc.count(status, keyword);
        int totalPage = Math.max(1, (int)Math.ceil((double)total / size));
        List<Orders> items = svc.findPaged(page, size, status, keyword);

        // 품목 요약(summary) 채워서 내려주기
        items.forEach(o -> o.setStatus(o.getStatus())); // NOP (직렬화 보장용)
        Map<String,Object> res = new HashMap<>();
        res.put("page", page);
        res.put("totalPage", totalPage);
        res.put("total", total);
        res.put("items", items);
        return res;
    }

    // 단건 + 아이템
    @GetMapping("/{orderId}")
    public Orders get(@PathVariable Long orderId){
        return svc.findOneWithItems(orderId);
    }

    // 상태 변경
    @PutMapping("/{orderId}/status")
    public void updateStatus(@PathVariable Long orderId, @RequestBody Map<String,String> body){
        String status = body.get("status");
        svc.updateStatus(orderId, status);
    }

    // 삭제 (PAID에서만)
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long orderId){
        boolean ok = svc.deleteIfPaid(orderId);
        if(!ok) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.CONFLICT, "PAID only");
    }
}