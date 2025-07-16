package member;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	
	@Autowired
	MemberMapper dao;

	public void save(RegisterForm form) {
		// 전달받은 값은 username, password, email전달 받았지만
		//추가적인 데이터를 Member에서 넣어서 전달
		Member member = new Member();
		BeanUtils.copyProperties(form, member);
		member.setRole("ROLE_USER");
//		new java.util.Date().getTime(); //long형임
//		new java.sql.Date(long형이 입력)
		member.setRegdate(new java.sql.Date(new java.util.Date().getTime()));
		dao.save(member);
	}

}
