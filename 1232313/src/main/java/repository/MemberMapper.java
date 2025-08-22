package repository;

import java.util.List;

import org.apache.ibatis.annotations.*;

import vo.Member;

@Mapper
public interface MemberMapper {

    /* 회원 저장 (BLOCKED 기본값 N, ROLE 기본값 CUSTOMER) */
    @Insert(
        "INSERT INTO users (user_id, login_id, name, email, password, hp, role, blocked) " +
        "VALUES (SEQ_USERS.NEXTVAL, #{loginId}, #{name}, #{email}, #{password}, #{hp}, " +
        "        NVL(#{role}, 'CUSTOMER'), NVL(#{blocked}, 'N'))"
    )
    @SelectKey(
        statement = "SELECT SEQ_USERS.CURRVAL FROM dual",
        keyProperty = "userId",
        before = false,
        resultType = int.class
    )
    int save(Member member);

    /* 전체 목록 */
    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role, blocked FROM users")
    List<Member> findAll();

    /* userId로 조회 */
    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role, blocked " +
            "FROM users WHERE user_id = #{userId}")
    Member findByUserId(Integer userId);

    /* 로그인 아이디로 조회 (스프링 시큐리티 사용) */
    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role, blocked " +
            "FROM users WHERE login_id = #{username}")
    Member findByUsername(String username);

    /* 프로필/비번 수정 (role/blocked는 null이면 기존값 유지) */
    @Update("UPDATE users SET " +
            "name = #{name}, email = #{email}, hp = #{hp}, password = #{password}, " +
            "role = NVL(#{role}, role), blocked = NVL(#{blocked}, blocked) " +
            "WHERE user_id = #{userId}")
    int update(Member member);

    /* 삭제 */
    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int delete(@Param("userId") Integer userId);

    /* 관리자: 권한만 변경 */
    @Update("UPDATE users SET role = #{role} WHERE user_id = #{userId}")
    int updateRole(@Param("userId") Integer userId, @Param("role") String role);

    /* 관리자: 차단/해제 */
    @Update("UPDATE users SET blocked = #{blocked} WHERE user_id = #{userId}")
    int updateBlocked(@Param("userId") Integer userId, @Param("blocked") String blocked);

    /* 전체 회원 수 */
    @Select("SELECT COUNT(*) FROM users")
    int countAll();
}