package login;


import java.util.ArrayList;
import java.util.List;

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

/**
 * 로그인 인증을 위한 사용자 정보를 불러오는 서비스 클래스
 * Spring Security가 로그인 시 호출하는 서비스
 */
@Service("loginService")
public class LoginService implements UserDetailsService {

	@Autowired
	SqlSessionFactory sqlSessionFactory;//db구축 방식

	/**
	 * 로그인 요청 시 호출됨. username(아이디)로 사용자 정보를 조회하고
	 * UserDetails 형태로 반환하여 Spring Security가 인증 가능하게 함.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 1) MyBatis를 사용해 DB 세션 열기
		SqlSession sqlSession = sqlSessionFactory.openSession();

		// 2) MemberMapper 인터페이스에서 DB 연동 객체 가져오기
		MemberMapper memberDao = sqlSession.getMapper(MemberMapper.class);

		// 3) 아이디(username)를 통해 사용자 정보 조회
		Member member = memberDao.findByUsername(username);

		 if (member == null) {
		        // 사용자 없으면 예외 던짐
		        throw new UsernameNotFoundException("User not found with username: " + username);
		    }
		 System.out.println("LoginId: " + member.getLoginId());
		 System.out.println("Password: " + member.getPassword());
		 System.out.println("Role: " + member.getRole());
		 
		// 4) 권한 리스트 생성
		List<GrantedAuthority> authorities = new ArrayList<>();

		// 사용자 역할(role)에 따라 권한 추가
		if (member.getRole().equals("ADMIN")) {
			// 관리자면 두 가지 권한 부여
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
			authorities.add(new SimpleGrantedAuthority("CUSTOMER"));
		} else if (member.getRole().equals("CUSTOMER")) {
			// 일반 사용자면 사용자 권한만
			authorities.add(new SimpleGrantedAuthority("CUSTOMER"));
		}

		// 5) UserDetails 객체 생성
		// 로그인 성공 시 Spring Security 내부에서 사용될 User 객체 리턴
		User user = new User(member.getLoginId(), member.getPassword(), authorities);

		// ※ 주의: 비밀번호는 BCrypt 등으로 암호화되어 있어야 함 (여기선 암호화 안 했다고 가정)

		return user;
	}
}