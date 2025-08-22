package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import service.AdminUserService;
import vo.Member;

@RestController
@RequestMapping("/admin/api/users")
public class AdminUserRestController {

    @Autowired
    private AdminUserService service;

    @GetMapping
    public Map<String,Object> list(@RequestParam(defaultValue="1") int page,
                                   @RequestParam(defaultValue="10") int size,
                                   @RequestParam(required=false) String keyword,
                                   @RequestParam(required=false) String role,
                                   @RequestParam(required=false) String blocked){
        int total = service.count(keyword, role, blocked);
        int totalPage = (int)Math.ceil(total / (double)size);
        List<Member> items = service.list(page, size, keyword, role, blocked);

        Map<String,Object> res = new HashMap<>();
        res.put("page", page);
        res.put("totalPage", totalPage);
        res.put("items", items);
        return res;
    }

    @PutMapping("/{id}/role")
    public void updateRole(@PathVariable("id") Long userId,
                           @RequestBody Map<String,String> body){
        service.updateRole(userId, body.get("role")); // 'ADMIN' or 'CUSTOMER'
    }

    @PutMapping("/{id}/block")
    public void updateBlocked(@PathVariable("id") Long userId,
                              @RequestBody Map<String,Object> body){
        boolean blocked = Boolean.parseBoolean(String.valueOf(body.get("blocked")));
        service.updateBlocked(userId, blocked);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId){
        service.delete(userId);
    }
}