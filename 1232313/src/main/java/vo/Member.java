package vo;

import lombok.Data;

@Data
public class Member {
    private Integer userId;     // user_id 而щ읆怨� 留ㅽ븨
    private String loginId;     // login_id 而щ읆怨� 留ㅽ븨
    private String password;
    private String name;
    private String email;
    private String hp;
    private String role;
    private String blocked;
}