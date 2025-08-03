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
	
	//member 
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
		Member member =  memberMapper.findByUsername(username);
		return member.getUser_id();
	}

	public Member login(Integer user_id, String password) {
		Member member = memberMapper.findByUserId(user_id);
		if (member != null && member.getPassword().equals(password)) {
			return member;
		}
		return null;
	}

	public boolean registerMember(Member member) {
		Member existingMember = memberMapper.findByUserId(member.getUser_id());
		if(existingMember != null) return false;
		member.setRole("ROLE_USER");
		PasswordEncoder pe = new BCryptPasswordEncoder();
		member.setPassword(pe.encode(member.getPassword() ));
		memberMapper.save(member);
		return true; 
	}
	
	public boolean registerAdmin(Member member) {
		Member existingMember = memberMapper.findByUserId(member.getUser_id());
		if(existingMember != null) return false;
		member.setRole("ROLE_ADMIN");
		PasswordEncoder pe = new BCryptPasswordEncoder();
		member.setPassword(pe.encode(member.getPassword() ));
		memberMapper.save(member);
		return true; 
	}
}