package repository;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;
import vo.OrderItem;
import vo.Orders;

@Mapper
public interface AdminOrderMapper {

    /* 총 개수 (상태/키워드 필터) */
    @Select(
      "SELECT COUNT(*) " +
      "FROM orders o " +
      "WHERE (#{status} IS NULL OR #{status} = '' OR o.status = #{status}) " +
      "  AND (#{keyword} IS NULL OR #{keyword} = '' " +
      "       OR TO_CHAR(o.order_id) LIKE '%'||#{keyword}||'%' " +
      "       OR TO_CHAR(o.user_id)  LIKE '%'||#{keyword}||'%')"
    )
    int count(@Param("status") String status,
              @Param("keyword") String keyword);

    /* 목록 (페이지네이션 + 요약 제목 LISTAGG) */
    @Select(
      "SELECT * FROM (" +
      "  SELECT " +
      "    o.order_id      AS orderId, " +
      "    o.user_id       AS userId, " +
      "    o.order_date    AS orderDate, " +
      "    o.total_amount  AS totalAmount, " +
      "    o.status        AS status, " +
      "    (SELECT LISTAGG(b.title, ', ') WITHIN GROUP(ORDER BY oi.order_item_id) " +
      "       FROM order_items oi JOIN books b ON b.book_id = oi.book_id " +
      "      WHERE oi.order_id = o.order_id) AS summary, " +
      "    ROW_NUMBER() OVER(ORDER BY o.order_date DESC, o.order_id DESC) rn " +
      "  FROM orders o " +
      "  WHERE (#{status} IS NULL OR #{status} = '' OR o.status = #{status}) " +
      "    AND (#{keyword} IS NULL OR #{keyword} = '' " +
      "         OR TO_CHAR(o.order_id) LIKE '%'||#{keyword}||'%' " +
      "         OR TO_CHAR(o.user_id)  LIKE '%'||#{keyword}||'%')" +
      ") WHERE rn BETWEEN (#{offset}+1) AND (#{offset}+#{size})"
    )
    @Results(id="OrderRow", value = {
      @Result(property="orderId",     column="orderId"),
      @Result(property="userId",      column="userId"),
      @Result(property="orderDate",   column="orderDate"),
      @Result(property="totalAmount", column="totalAmount"),
      @Result(property="status",      column="status"),
      @Result(property="summary",     column="summary")
    })
    List<Orders> list(@Param("status") String status,
                      @Param("keyword") String keyword,
                      @Param("offset") int offset,
                      @Param("size") int size);

    /* 단건 헤더 */
    @Select("SELECT order_id AS orderId, user_id AS userId, order_date AS orderDate, total_amount AS totalAmount, status " +
            "FROM orders WHERE order_id = #{orderId}")
    Orders findHeader(@Param("orderId") Long orderId);

    /* 단건 아이템 (기존 OrderMapper와 동일 시그니처를 넣어둠) */
    @Select("SELECT oi.order_item_id AS orderItemId, oi.order_id AS orderId, oi.book_id AS bookId, " +
            "       oi.quantity AS quantity, oi.price AS price, b.title AS title, b.cover_image AS coverImage " +
            "  FROM order_items oi JOIN books b ON b.book_id = oi.book_id " +
            " WHERE oi.order_id = #{orderId} ORDER BY oi.order_item_id")
    List<OrderItem> findItems(@Param("orderId") Long orderId);

    /* 상태 변경 */
    @Update("UPDATE orders SET status = #{status} WHERE order_id = #{orderId}")
    int updateStatus(@Param("orderId") Long orderId, @Param("status") String status);

    /* 삭제 (아이템 먼저 삭제 → 헤더 삭제). 상태가 PAID일 때만 허용 */
    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    int deleteItems(@Param("orderId") Long orderId);

    @Delete("DELETE FROM orders WHERE order_id = #{orderId} AND status = 'PAID'")
    int deleteOrderIfPaid(@Param("orderId") Long orderId);
}