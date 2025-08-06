package repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import vo.Member;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO users(login_id, name, email, password, hp, role) "
          + "VALUES (#{loginId, jdbcType=VARCHAR}, #{name, jdbcType=VARCHAR}, #{email, jdbcType=VARCHAR}, "
          + "#{password, jdbcType=VARCHAR}, #{hp, jdbcType=VARCHAR}, #{role, jdbcType=VARCHAR})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int save(Member member);

    @Select("SELECT * FROM users")
    List<Member> findAll();

    @Select("SELECT * FROM users WHERE user_id = #{userId, jdbcType=INTEGER}")
    Member findByUserId(Integer userId);

    @Select("SELECT user_id, login_id AS loginId, name, email, password, hp, role FROM users WHERE login_id = #{username}")
    Member findByUsername(String username);

    @Update("UPDATE users SET name = #{name}, "
          + "email = #{email}, "
          + "hp = #{hp}, "
          + "password = #{password} "
          + "WHERE user_id = #{userId}")
    int update(Member member);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int delete(@Param("userId") int userId);
}