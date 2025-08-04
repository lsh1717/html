package login;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomerLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//System.out.println("성공시 : " + authentication.getAuthorities());
		
		Set<String> roleNames = authentication.getAuthorities().stream()
			    .map(GrantedAuthority::getAuthority)
			    .collect(Collectors.toSet());

		if(roleNames.contains("ADMIN")) {
			response.sendRedirect("/manager/bookList"); //pageController에서 모두 처리
		}else if(roleNames.contains("CUSTOMER")) {
			response.sendRedirect("/user/bookList");
		}else {
			response.sendRedirect("/login/login");
		}
		
	}

}