package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import repository.MemberMapper;
import vo.Member;

@Service
public class MemberService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    public int saveMember(Member member) {
        return memberMapper.save(member);
    }
    
    public Member getMember(int id) {
        return memberMapper.findByUserId(id);
    }
    
    public List<Member> getMemberList() {
        return memberMapper.findAll();
    }
    
    public int updateMember(Member member) {
        return memberMapper.update(member);
    }
    
    public int deleteMember(int id) {
        return memberMapper.delete(id);
    }
    
    public int findId(String username) {
        Member member = memberMapper.findByUsername(username);
        if(member == null) return -1; // 없으면 -1 리턴
        return member.getUserId();
    }

    public Member login(Integer user_id, String password) {
        Member member = memberMapper.findByUserId(user_id);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    // 회원가입 처리: 로그인 아이디(loginId) 중복 체크
    public boolean registerMember(Member member) {
        Member existingMember = memberMapper.findByUsername(member.getLoginId());
        if(existingMember != null) return false;

        // 폼에서 넘어온 role을 그대로 저장
        // 만약 role이 null 이면 기본값으로 "ROLE_USER" 설정
        if (member.getRole() == null || member.getRole().isEmpty()) {
            member.setRole("CUSTOMER");
        }

        PasswordEncoder pe = new BCryptPasswordEncoder();
        member.setPassword(pe.encode(member.getPassword()));

        memberMapper.save(member);
        return true;
    }
    
    public boolean registerAdmin(Member member) {
        Member existingMember = memberMapper.findByUsername(member.getLoginId());
        if(existingMember != null) return false;

        member.setRole("ADMIN");

        PasswordEncoder pe = new BCryptPasswordEncoder();
        member.setPassword(pe.encode(member.getPassword()));

        memberMapper.save(member);
        return true; 
    }
}