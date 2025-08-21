package repository;

import java.util.List;

import org.apache.ibatis.annotations.*;

import vo.Member;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO users (user_id, login_id, name, email, password, hp, role) " +
            "VALUES (SEQ_USERS.NEXTVAL, #{loginId}, #{name}, #{email}, #{password}, #{hp}, #{role})")
    @SelectKey(statement = "SELECT SEQ_USERS.CURRVAL FROM dual", keyProperty = "userId", before = false, resultType = int.class)
    int save(Member member);

    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role FROM users")
    List<Member> findAll();

    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role FROM users WHERE user_id = #{userId}")
    Member findByUserId(Integer userId);

    @Select("SELECT user_id AS userId, login_id AS loginId, name, email, password, hp, role FROM users WHERE login_id = #{username}")
    Member findByUsername(String username);

    @Update("UPDATE users SET name = #{name}, email = #{email}, hp = #{hp}, password = #{password} WHERE user_id = #{userId}")
    int update(Member member);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int delete(@Param("userId") Integer userId);

    // �윋� �쟾泥� �쉶�썝 �닔 (���떆蹂대뱶�슜)
    @Select("SELECT COUNT(*) FROM users")
    int countAll();
    
    
}