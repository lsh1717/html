package vo;

import lombok.Data;

@Data
public class Member {
    private Integer userId;     // user_id 컬럼과 매핑
    private String loginId;     // login_id 컬럼과 매핑
    private String password;
    private String name;
    private String email;
    private String hp;
    private String role;
}