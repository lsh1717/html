package login;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import repository.MemberMapper;
import vo.Member;

/**
 * 로그인 시 사용자 정보를 로드하는 서비스.
 * - ROLE: DB의 ADMIN / CUSTOMER 값을 "ROLE_*" 로 변환
 * - BLOCKED: 'Y' 면 enabled=false, accountNonLocked=false 로 반환 -> 로그인 차단
 */
@Service("loginService")
public class LoginService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Autowired
    public LoginService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loginId 로 사용자 조회 (메서드명이 다르면 findByLoginId 로 바꿔도 됨)
        Member m = memberMapper.findByUsername(username);
        if (m == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // BLOCKED 처리: 'Y' 이면 로그인 불가
        boolean blocked = "Y".equalsIgnoreCase(m.getBlocked());

        // 권한 매핑 (스프링 기본 RoleVoter 는 'ROLE_' prefix 를 기대함)
        String role = (m.getRole() == null ? "CUSTOMER" : m.getRole().toUpperCase());
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        // User(enabled, accountNonExpired, credentialsNonExpired, accountNonLocked)
        return new org.springframework.security.core.userdetails.User(
                m.getLoginId(),                 // username
                m.getPassword(),                // password(BCrypt)
                !blocked,                       // enabled        (차단이면 false)
                true,                           // accountNonExpired
                true,                           // credentialsNonExpired
                !blocked,                       // accountNonLocked (차단이면 false)
                authorities
        );
    }
}