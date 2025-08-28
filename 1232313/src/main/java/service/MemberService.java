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

    // PasswordEncoder Bean이 등록되어 있지 않아도 정상동작하도록 안전장치
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    private PasswordEncoder pe() {
        return (passwordEncoder != null) ? passwordEncoder : new BCryptPasswordEncoder();
    }

    /** 공통: null/공백 기본값 보정 */
    private void normalizeDefaults(Member m) {
        if (m == null) return;
        // role 기본값
        if (m.getRole() == null || m.getRole().trim().isEmpty()) {
            m.setRole("CUSTOMER");
        }
        // blocked 기본값 (Oracle에서 null 바인딩 시 ORA-17004 예방)
        if (m.getBlocked() == null || m.getBlocked().trim().isEmpty()) {
            m.setBlocked("N");
        }
    }

    /** 공통: 패스워드가 평문이면 해시, 이미 해시면 그대로 둠 */
    private void ensurePasswordEncoded(Member m) {
        if (m == null) return;
        String pw = m.getPassword();
        if (pw == null || pw.isEmpty()) return;
        // bcrypt 형태인지 대략 판별 ($2a, $2b, $2y)
        boolean looksHashed = pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$");
        if (!looksHashed) {
            m.setPassword(pe().encode(pw));
        }
    }

    /* ========= CRUD ========= */

    public int saveMember(Member member) {
        normalizeDefaults(member);
        ensurePasswordEncoded(member); // 신규 저장시 평문이면 해시
        return memberMapper.save(member);
    }

    public Member getMember(int id) {
        return memberMapper.findByUserId(id);
    }

    public List<Member> getMemberList() {
        return memberMapper.findAll();
    }

    public int updateMember(Member member) {
        normalizeDefaults(member);
        // 업데이트 시 패스워드가 넘어왔다면 평문 여부에 따라 해시
        ensurePasswordEncoded(member);
        return memberMapper.update(member);
    }

    public int deleteMember(int id) {
        return memberMapper.delete(id);
    }

    /* ========= 조회/로그인 ========= */

    public int findId(String username) {
        Member member = memberMapper.findByUsername(username);
        if (member == null) return -1;
        return member.getUserId();
    }

    public Member login(Integer user_id, String rawPassword) {
        Member member = memberMapper.findByUserId(user_id);
        if (member != null && rawPassword != null) {
            // 평문(raw) vs 해시(hashed) 비교
            if (pe().matches(rawPassword, member.getPassword())) {
                return member;
            }
        }
        return null;
    }

    /* ========= 회원가입 ========= */

    public boolean registerMember(Member member) {
        // 중복 로그인ID 체크
        Member existing = memberMapper.findByUsername(member.getLoginId());
        if (existing != null) return false;

        normalizeDefaults(member);     // role= CUSTOMER, blocked= N
        ensurePasswordEncoded(member); // 비번 해시
        memberMapper.save(member);
        return true;
    }

    public boolean registerAdmin(Member member) {
        Member existing = memberMapper.findByUsername(member.getLoginId());
        if (existing != null) return false;

        member.setRole("ADMIN");
        normalizeDefaults(member);     // blocked= N 등
        ensurePasswordEncoded(member); // 비번 해시
        memberMapper.save(member);
        return true;
    }

    public Member getMemberByLoginId(String loginId) {
        return memberMapper.findByUsername(loginId);
    }

    // 📊 대시보드용 카운트
    public int countUsers() {
        return memberMapper.countAll();
    }
}