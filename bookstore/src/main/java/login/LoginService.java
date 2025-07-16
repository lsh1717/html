package login;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//@Service
public class LoginService implements UserDetailsService {
	
	public LoginService() {
		System.out.println("로그인 서비스 생성자 생성 !!");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("로그인 폼으로 부터 전달된 username:"+username);
		return null;
	}
}
