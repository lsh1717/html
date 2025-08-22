package repository;

import java.util.List;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import vo.Member;

@Mapper
public interface AdminUserMapper {

  /* 총 개수 */
  @Select({
    "<script>",
    "SELECT COUNT(*)",
    "FROM USERS u",
    "<where>",
    "  <if test='keyword != null and keyword != \"\"'>",
    "    ( u.LOGIN_ID LIKE '%'||#{keyword}||'%'",
    "      OR u.NAME   LIKE '%'||#{keyword}||'%'",
    "      OR u.EMAIL  LIKE '%'||#{keyword}||'%' )",
    "  </if>",
    "  <if test='role != null and role != \"\"'>",
    "    AND u.ROLE = #{role}",
    "  </if>",
    "  <if test='blocked != null and blocked != \"\"'>",
    "    AND u.BLOCKED = #{blocked}",
    "  </if>",
    "</where>",
    "</script>"
  })
  int count(@Param("keyword") String keyword,
            @Param("role")    String role,
            @Param("blocked") String blocked);

  /* 목록(페이징) */
  @Select({
    "<script>",
    "SELECT * FROM (",
    "  SELECT t.*, ROWNUM rn FROM (",
    "    SELECT",
    "      u.USER_ID  AS userId,",
    "      u.LOGIN_ID AS loginId,",
    "      u.NAME     AS name,",
    "      u.EMAIL    AS email,",
    "      u.HP       AS hp,",
    "      u.ROLE     AS role,",
    "      u.BLOCKED  AS blocked",
    "    FROM USERS u",
    "    <where>",
    "      <if test='keyword != null and keyword != \"\"'>",
    "        ( u.LOGIN_ID LIKE '%'||#{keyword}||'%'",
    "          OR u.NAME   LIKE '%'||#{keyword}||'%'",
    "          OR u.EMAIL  LIKE '%'||#{keyword}||'%' )",
    "      </if>",
    "      <if test='role != null and role != \"\"'>",
    "        AND u.ROLE = #{role}",
    "      </if>",
    "      <if test='blocked != null and blocked != \"\"'>",
    "        AND u.BLOCKED = #{blocked}",
    "      </if>",
    "    </where>",
    "    ORDER BY u.USER_ID DESC",
    "  ) t",
    "  WHERE ROWNUM &lt;= #{offset} + #{size}",
    ")",
    "WHERE rn &gt; #{offset}",
    "</script>"
  })
  @Results(id="UserMap", value = {
    @Result(column="userId",  property="userId",  id=true, jdbcType=JdbcType.BIGINT),
    @Result(column="loginId", property="loginId"),
    @Result(column="name",    property="name"),
    @Result(column="email",   property="email"),
    @Result(column="hp",      property="hp"),
    @Result(column="role",    property="role"),
    @Result(column="blocked", property="blocked")
  })
  List<Member> list(@Param("offset") int offset,
                    @Param("size")   int size,
                    @Param("keyword") String keyword,
                    @Param("role")    String role,
                    @Param("blocked") String blocked);

  /* 권한 변경 */
  @Update("UPDATE USERS SET ROLE = #{role} WHERE USER_ID = #{userId}")
  int updateRole(@Param("userId") Long userId, @Param("role") String role);

  /* 차단/해제 */
  @Update("UPDATE USERS SET BLOCKED = #{blocked} WHERE USER_ID = #{userId}")
  int updateBlocked(@Param("userId") Long userId, @Param("blocked") String blocked); // 'Y' or 'N'

  /* 삭제 */
  @Delete("DELETE FROM USERS WHERE USER_ID = #{userId}")
  int delete(@Param("userId") Long userId);
}