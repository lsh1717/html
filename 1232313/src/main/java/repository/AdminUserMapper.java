package repository;

import org.apache.ibatis.annotations.*;
import java.util.List;
import vo.Member;

@Mapper
public interface AdminUserMapper {

    /* 총 개수 */
    @Select(
      "SELECT COUNT(*) FROM users u " +
      "WHERE (#{keyword} IS NULL OR #{keyword}='' " +
      "       OR u.login_id LIKE '%'||#{keyword}||'%' " +
      "       OR u.name     LIKE '%'||#{keyword}||'%' " +
      "       OR u.email    LIKE '%'||#{keyword}||'%')" +
      "  AND (#{role} IS NULL OR #{role}='' OR u.role = #{role}) " +
      "  AND (#{blocked} IS NULL OR #{blocked}='' OR u.blocked = #{blocked})"
    )
    int count(@Param("keyword") String keyword,
              @Param("role")    String role,
              @Param("blocked") String blocked);

    /* 목록 (페이지네이션) */
    @Select(
      "SELECT * FROM (" +
      "  SELECT u.user_id AS userId, u.login_id AS loginId, u.name, u.role, u.blocked, " +
      "         ROW_NUMBER() OVER(ORDER BY u.user_id DESC) rn " +
      "  FROM users u " +
      "  WHERE (#{keyword} IS NULL OR #{keyword}='' " +
      "         OR u.login_id LIKE '%'||#{keyword}||'%' " +
      "         OR u.name     LIKE '%'||#{keyword}||'%' " +
      "         OR u.email    LIKE '%'||#{keyword}||'%')" +
      "    AND (#{role} IS NULL OR #{role}='' OR u.role = #{role}) " +
      "    AND (#{blocked} IS NULL OR #{blocked}='' OR u.blocked = #{blocked})" +
      ") WHERE rn BETWEEN (#{offset}+1) AND (#{offset}+#{size})"
    )
    List<Member> list(@Param("keyword") String keyword,
                      @Param("role")    String role,
                      @Param("blocked") String blocked,
                      @Param("offset")  int offset,
                      @Param("size")    int size);

    /* 권한 변경 */
    @Update("UPDATE users SET role = #{role} WHERE user_id = #{userId}")
    int updateRole(@Param("userId") Integer userId, @Param("role") String role);

    /* 차단/해제 (Y/N) */
    @Update("UPDATE users SET blocked = #{blocked} WHERE user_id = #{userId}")
    int updateBlocked(@Param("userId") Integer userId, @Param("blocked") String blocked);

    /* 삭제 */
    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    int delete(@Param("userId") Integer userId);
}