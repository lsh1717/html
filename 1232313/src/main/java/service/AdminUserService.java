package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.AdminUserMapper;
import vo.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper mapper;

    public Map<String,Object> list(int page, int size, String role, String blocked, String keyword){
        int total = mapper.count(keyword, role, blocked);
        int totalPage = (int)Math.ceil(total / (double)size);
        int offset = (page-1) * size;
        List<Member> rows = mapper.list(keyword, role, blocked, offset, size);

        Map<String,Object> resp = new HashMap<>();
        resp.put("page", page);
        resp.put("size", size);
        resp.put("totalCount", total);
        resp.put("totalPage", totalPage);
        resp.put("items", rows);
        return resp;
    }

    public boolean updateRole(Integer userId, String role){
        return mapper.updateRole(userId, role) > 0;
    }

    public boolean updateBlocked(Integer userId, boolean blocked){
        return mapper.updateBlocked(userId, blocked ? "Y" : "N") > 0;
    }

    public boolean delete(Integer userId){
        return mapper.delete(userId) > 0;
    }
}