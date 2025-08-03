package vo;

import lombok.Data;

@Data
public class Member {
	private Integer user_id;		
	private String login_id;
	private String password;
	private String name;
	private String email;
	private String hp;
	private String role;
}