package service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.AdminUserMapper;
import vo.Member;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserMapper mapper;

    public int count(String keyword, String role, String blocked){
        return mapper.count(keyword, role, blocked);
    }

    public List<Member> list(int page, int size, String keyword, String role, String blocked){
        int offset = (Math.max(page,1) - 1) * Math.max(size,1);
        return mapper.list(offset, size, keyword, role, blocked);
    }

    public void updateRole(Long userId, String role){
        mapper.updateRole(userId, role);
    }

    public void updateBlocked(Long userId, boolean blocked){
        mapper.updateBlocked(userId, blocked ? "Y" : "N");
    }

    public void delete(Long userId){
        mapper.delete(userId);
    }
}