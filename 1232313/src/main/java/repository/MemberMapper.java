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
		      + "VALUES (#{login_id, jdbcType=VARCHAR}, #{name, jdbcType=VARCHAR}, #{email, jdbcType=VARCHAR}, "
		      + "#{password, jdbcType=VARCHAR}, #{hp, jdbcType=VARCHAR}, #{role, jdbcType=VARCHAR})")
		@Options(useGeneratedKeys=true, keyProperty="user_id", keyColumn="user_id")
		int save(Member member);
	
    @Select("SELECT * FROM users")
    List<Member> findAll();

    @Select("SELECT * FROM users WHERE user_id = #{user_id, jdbcType=INTEGER}")
    Member findByUserId(Integer user_id);

    @Select("SELECT * FROM users WHERE login_id = #{username}")
    Member findByUsername(String username);

    @Update("UPDATE users SET name = #{name}, "
            + "email = #{email}, "
            + "phone_number = #{hp}, "
            + "password = #{password} "
            + "WHERE user_id = #{user_id}")
      int update(Member member);

    @Delete("DELETE FROM users WHERE user_id = #{user_id}")
    int delete(@Param("user_id") int user_id);
    
    
   
    
    
}