package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AdminUserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/users")
public class AdminUserRestController {

    private final AdminUserService service;

    /* 목록 */
    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String role,
                                   @RequestParam(required=false) String blocked,
                                   @RequestParam(required=false) String keyword){
        return service.list(page, size, role, blocked, keyword);
    }

    /* 권한 변경 */
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateRole(@PathVariable Integer userId,
                                        @RequestBody Map<String,String> body){
        boolean ok = service.updateRole(userId, body.get("role"));
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /* 차단/해제 */
    @PutMapping("/{userId}/block")
    public ResponseEntity<?> updateBlocked(@PathVariable Integer userId,
                                           @RequestBody Map<String,Object> body){
        Object v = body.get("blocked");
        boolean blocked = "Y".equalsIgnoreCase(String.valueOf(v)) ||
                          Boolean.TRUE.equals(v) || "true".equalsIgnoreCase(String.valueOf(v));
        boolean ok = service.updateBlocked(userId, blocked);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /* 삭제 */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer userId){
        boolean ok = service.delete(userId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(409).build();
    }
}