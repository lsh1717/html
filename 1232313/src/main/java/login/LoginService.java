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
 * �α��� �� ����� ������ �ε��ϴ� ����.
 * - ROLE: DB�� ADMIN / CUSTOMER ���� "ROLE_*" �� ��ȯ
 * - BLOCKED: 'Y' �� enabled=false, accountNonLocked=false �� ��ȯ -> �α��� ����
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
        // loginId �� ����� ��ȸ (�޼������ �ٸ��� findByLoginId �� �ٲ㵵 ��)
        Member m = memberMapper.findByUsername(username);
        if (m == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // BLOCKED ó��: 'Y' �̸� �α��� �Ұ�
        boolean blocked = "Y".equalsIgnoreCase(m.getBlocked());

        // ���� ���� (������ �⺻ RoleVoter �� 'ROLE_' prefix �� �����)
        String role = (m.getRole() == null ? "CUSTOMER" : m.getRole().toUpperCase());
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        // User(enabled, accountNonExpired, credentialsNonExpired, accountNonLocked)
        return new org.springframework.security.core.userdetails.User(
                m.getLoginId(),                 // username
                m.getPassword(),                // password(BCrypt)
                !blocked,                       // enabled        (�����̸� false)
                true,                           // accountNonExpired
                true,                           // credentialsNonExpired
                !blocked,                       // accountNonLocked (�����̸� false)
                authorities
        );
    }
}