package repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import vo.OrderItem;
import vo.Orders;

@Mapper
public interface OrderMapper {

    /* ======================== 생성 ======================== */

    // 주문 생성
    @Insert("INSERT INTO orders (order_id, user_id, order_date, total_amount, status) " +
            "VALUES (SEQ_ORDERS.NEXTVAL, #{userId}, SYSDATE, #{totalAmount}, #{status})")
    @SelectKey(statement = "SELECT SEQ_ORDERS.CURRVAL FROM dual",
               keyProperty = "orderId", before = false, resultType = long.class)
    void insertOrder(Orders order);

    // 주문 아이템 생성 (price = 주문 시점의 단가)
    @Insert("INSERT INTO order_items (order_item_id, order_id, book_id, quantity, price) " +
            "VALUES (SEQ_ORDER_ITEMS.NEXTVAL, #{orderId}, #{bookId}, #{quantity}, #{price})")
    @SelectKey(statement = "SELECT SEQ_ORDER_ITEMS.CURRVAL FROM dual",
               keyProperty = "orderItemId", before = false, resultType = long.class)
    void insertOrderItem(OrderItem orderItem);

    /* ======================== 조회 ======================== */

    // 회원별 주문 + 아이템 목록
    @Select("SELECT o.order_id AS orderId, o.user_id AS userId, o.order_date AS orderDate, " +
            "       o.total_amount AS totalAmount, o.status AS status " +
            "FROM orders o " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.order_date DESC, o.order_id DESC")
    @Results(id = "OrdersWithItems", value = {
        @Result(property = "orderId",     column = "orderId"),
        @Result(property = "userId",      column = "userId"),
        @Result(property = "orderDate",   column = "orderDate"),
        @Result(property = "totalAmount", column = "totalAmount"),
        @Result(property = "status",      column = "status"),
        @Result(property = "items",       javaType = List.class, column = "orderId",
                many = @Many(select = "findOrderItemsByOrderId"))
    })
    List<Orders> findOrdersWithItems(Long userId);

    // 주문별 아이템 조회 (+ 도서 제목/표지)
    @Select("SELECT oi.order_item_id AS orderItemId, " +
            "       oi.order_id      AS orderId, " +
            "       oi.book_id       AS bookId, " +
            "       oi.quantity      AS quantity, " +
            "       oi.price         AS price, " +
            "       b.book_id        AS bookId2, " +   // 충돌 방지용 별칭
            "       b.title          AS title, " +
            "       b.cover_image    AS coverImage " +
            "FROM order_items oi " +
            "JOIN books b ON oi.book_id = b.book_id " +
            "WHERE oi.order_id = #{orderId} " +
            "ORDER BY oi.order_item_id")
    @Results(id = "OrderItemWithBook", value = {
        @Result(property = "orderItemId",     column = "orderItemId"),
        @Result(property = "orderId",         column = "orderId"),
        @Result(property = "bookId",          column = "bookId"),
        @Result(property = "quantity",        column = "quantity"),
        @Result(property = "price",           column = "price"),
        @Result(property = "book.bookId",     column = "bookId2"),
        @Result(property = "book.title",      column = "title"),
        @Result(property = "book.coverImage", column = "coverImage")
    })
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    // 최근 주문 n개 (간단 헤더용)
    @Select("SELECT order_id AS orderId, user_id AS userId, order_date AS orderDate, " +
            "       total_amount AS totalAmount, status AS status " +
            "FROM (SELECT * FROM orders ORDER BY order_date DESC, order_id DESC) " +
            "WHERE ROWNUM <= #{limit}")
    List<Orders> findRecentOrders(@Param("limit") int limit);

    // 최근 주문 n개 (대시보드 표용)
    @Select("SELECT * FROM ( " +
            "  SELECT o.order_id AS orderId, o.user_id AS userId, o.total_amount AS totalAmount, " +
            "         o.status AS status, o.order_date AS orderDate " +
            "  FROM orders o " +
            "  ORDER BY o.order_date DESC, o.order_id DESC " +
            ") WHERE ROWNUM <= #{limit}")
    List<Orders> findRecent(@Param("limit") int limit);

    /* ======================== 통계(KPI/차트) ======================== */

    // 오늘 주문 건수
    @Select("SELECT COUNT(*) FROM orders WHERE TRUNC(order_date) = TRUNC(SYSDATE)")
    int countTodayOrders();

    // 오늘 매출(취소 제외) — CANCELLED 제외, NULL status는 PAID로 간주
    @Select("SELECT NVL(SUM(total_amount),0) FROM orders " +
            "WHERE TRUNC(order_date)=TRUNC(SYSDATE) " +
            "  AND NVL(status,'PAID') <> 'CANCELLED'")
    Integer sumTodayRevenue();

    // 최근 N개월 월별 매출(취소 제외) — 라벨 'MM월'
    @Select("SELECT TO_CHAR(TRUNC(order_date,'MM'),'MM\"월\"') AS label, " +
            "       SUM(total_amount) AS value, " +
            "       TRUNC(order_date,'MM') AS mon " +
            "FROM orders " +
            "WHERE order_date >= ADD_MONTHS(TRUNC(SYSDATE,'MM'), -(#{months}-1)) " +
            "  AND NVL(status,'PAID') <> 'CANCELLED' " +
            "GROUP BY TRUNC(order_date,'MM') " +
            "ORDER BY mon")
    List<Map<String,Object>> monthlyRevenue(@Param("months") int months);

    // 주문 상태 분포 — 4단계(PAID/SHIPPING/DELIVERED/CANCELLED)
    @Select("SELECT label, COUNT(*) AS value FROM ( " +
            "  SELECT CASE " +
            "           WHEN NVL(status,'PAID') = 'PAID'  THEN 'PAID' " +
            "           WHEN status = 'SHIPPING'          THEN 'SHIPPING' " +
            "           WHEN status = 'DELIVERED'         THEN 'DELIVERED' " +
            "           WHEN status = 'CANCELLED'         THEN 'CANCELLED' " +
            "           ELSE 'PAID' " +
            "         END AS label " +
            "  FROM orders " +
            ") GROUP BY label")
    List<Map<String,Object>> statusCounts();

    /* ====== 새로 추가: 대시보드 정합성 (출고 대기/배송 중, 일매출) ====== */

    // 상태별 주문 건수 (출고 대기=PAID, 배송중=SHIPPING)
    @Select("SELECT COUNT(*) FROM orders WHERE NVL(status,'PAID') = #{status}")
    Integer countByStatus(@Param("status") String status);

    // 최근 N일 '일자별 매출'(취소 제외) — 라벨 'MM.DD', 누락일은 0으로 채움
    @Select({
        "WITH days AS (",
        "  SELECT TRUNC(SYSDATE) - LEVEL + 1 AS d",
        "  FROM dual",
        "  CONNECT BY LEVEL <= #{days}",
        "), agg AS (",
        "  SELECT TRUNC(order_date) AS d, SUM(total_amount) AS amount",
        "  FROM orders",
        "  WHERE order_date >= TRUNC(SYSDATE) - (#{days}-1)",
        "    AND NVL(status,'PAID') <> 'CANCELLED'",
        "  GROUP BY TRUNC(order_date)",
        ")",
        "SELECT TO_CHAR(d.d, 'MM\".\"DD') AS label,",
        "       NVL(agg.amount, 0)        AS value,",
        "       d.d                        AS day",
        "FROM days d",
        "LEFT JOIN agg ON agg.d = d.d",
        "ORDER BY d.d"
    })
    List<Map<String,Object>> dailyRevenue(@Param("days") int days);
}