package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AdminOrderService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/orders")
public class AdminOrderRestController {

    private final AdminOrderService service;

    /* 목록 */
    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String status,
                                   @RequestParam(required=false) String keyword){
        return service.list(page, size, status, keyword);
    }

    /* 상세 (아이템 포함) */
    @GetMapping("/{orderId}")
    public Map<String,Object> detail(@PathVariable Long orderId){
        return service.detail(orderId);
    }

    /* 상태 변경 */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long orderId,
                                          @RequestBody Map<String,String> body){
        boolean ok = service.updateStatus(orderId, body.get("status"));
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /* 삭제 (PAID만 가능 → 실패 시 409) */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> delete(@PathVariable Long orderId){
        int deleted = service.deleteIfPaid(orderId);
        return deleted > 0 ? ResponseEntity.ok().build() : ResponseEntity.status(409).build();
    }
}