package login;

import java.util.List;
import java.util.Collections;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import repository.MemberMapper;
import vo.Member;

@Service("loginService")
public class LoginService implements UserDetailsService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            MemberMapper memberDao = sqlSession.getMapper(MemberMapper.class);

            Member member = memberDao.findByUsername(username);

            if (member == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            System.out.println("LoginId: " + member.getLoginId());
            System.out.println("Password: " + member.getPassword());
            System.out.println("Role: " + member.getRole());

            // ✅ DB 값("ADMIN", "CUSTOMER")에 ROLE_ prefix 붙여주기
            String roleName = "ROLE_" + member.getRole().toUpperCase();

            List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(roleName));

            return new User(member.getLoginId(), member.getPassword(), authorities);
        }
    }
}