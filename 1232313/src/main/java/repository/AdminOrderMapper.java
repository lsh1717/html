package repository;

import java.util.List;

import org.apache.ibatis.annotations.*;

import vo.Orders;

@Mapper
public interface AdminOrderMapper {

    // 총 개수
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM orders o",
        "<where>",
        "  <if test='status != null and status != \"\"'> AND o.status = #{status} </if>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    AND (TO_CHAR(o.order_id) LIKE '%'||#{keyword}||'%'",
        "         OR TO_CHAR(o.user_id) LIKE '%'||#{keyword}||'%')",
        "  </if>",
        "</where>",
        "</script>"
    })
    int count(@Param("status") String status, @Param("keyword") String keyword);

    // 페이지 조회(+ 품목수/요약은 별도 쿼리로 처리해도 충분)
    @Select({
        "<script>",
        "SELECT o.order_id AS orderId, o.user_id AS userId, o.order_date AS orderDate, ",
        "       o.total_amount AS totalAmount, o.status AS status",
        "FROM orders o",
        "<where>",
        "  <if test='status != null and status != \"\"'> AND o.status = #{status} </if>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    AND (TO_CHAR(o.order_id) LIKE '%'||#{keyword}||'%'",
        "         OR TO_CHAR(o.user_id) LIKE '%'||#{keyword}||'%')",
        "  </if>",
        "</where>",
        "ORDER BY o.order_date DESC",
        "OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY",
        "</script>"
    })
    List<Orders> findPaged(@Param("offset") int offset,
                           @Param("pageSize") int pageSize,
                           @Param("status") String status,
                           @Param("keyword") String keyword);

    // 단건(헤더)
    @Select("SELECT o.order_id AS orderId, o.user_id AS userId, o.order_date AS orderDate, o.total_amount AS totalAmount, o.status AS status FROM orders o WHERE o.order_id=#{orderId}")
    Orders findOne(@Param("orderId") Long orderId);

    // 간단 요약(첫 아이템 제목 + 외 N개) — 리스트 표시 최적화 용
    @Select({
        "SELECT CASE",
        "  WHEN COUNT(*)=0 THEN ''",
        "  WHEN COUNT(*)=1 THEN MAX(b.title)",
        "  ELSE MAX(b.title) || ' 외 ' || (COUNT(*)-1) || '개'",
        "END AS summary",
        "FROM order_items oi JOIN books b ON oi.book_id=b.book_id",
        "WHERE oi.order_id=#{orderId}"
    })
    String summary(@Param("orderId") Long orderId);

    // 상태 단건 조회
    @Select("SELECT status FROM orders WHERE order_id=#{orderId}")
    String getStatus(@Param("orderId") Long orderId);

    // 상태 변경
    @Update("UPDATE orders SET status=#{status} WHERE order_id=#{orderId}")
    int updateStatus(@Param("orderId") Long orderId, @Param("status") String status);

    // 삭제(아이템→헤더 순, 헤더는 PAID일 때만)
    @Delete("DELETE FROM order_items WHERE order_id=#{orderId}")
    int deleteItems(@Param("orderId") Long orderId);

    @Delete("DELETE FROM orders WHERE order_id=#{orderId} AND status='PAID'")
    int deleteOrderIfPaid(@Param("orderId") Long orderId);
}