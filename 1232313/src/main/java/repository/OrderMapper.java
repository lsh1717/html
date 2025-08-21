package repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import vo.OrderItem;
import vo.Orders;

@Mapper
public interface OrderMapper {

    // 二쇰Ц �깮�꽦
    @Insert("INSERT INTO orders (order_id, user_id, order_date, total_amount, status) " +
            "VALUES (SEQ_ORDERS.NEXTVAL, #{userId}, SYSDATE, #{totalAmount}, #{status})")
    @SelectKey(statement = "SELECT SEQ_ORDERS.CURRVAL FROM dual", keyProperty = "orderId", before = false, resultType = long.class)
    void insertOrder(Orders order);

    // 二쇰Ц �븘�씠�뀥 �깮�꽦
    @Insert("INSERT INTO order_items (order_item_id, order_id, book_id, quantity, price) " +
            "VALUES (SEQ_ORDER_ITEMS.NEXTVAL, #{orderId}, #{bookId}, #{quantity}, #{price})")
    @SelectKey(statement = "SELECT SEQ_ORDER_ITEMS.CURRVAL FROM dual", keyProperty = "orderItemId", before = false, resultType = long.class)
    void insertOrderItem(OrderItem orderItem);

    // �쉶�썝蹂� 二쇰Ц�궡�뿭
    @Select("SELECT o.order_id AS orderId, o.user_id AS userId, o.order_date AS orderDate, o.total_amount AS totalAmount, o.status AS status " +
            "FROM orders o WHERE o.user_id = #{userId} ORDER BY o.order_date DESC")
    @Results(id = "OrdersWithItems", value = {
            @Result(property = "orderId", column = "orderId"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "orderDate", column = "orderDate"),
            @Result(property = "totalAmount", column = "totalAmount"),
            @Result(property = "status", column = "status"),
            @Result(property = "items", javaType = List.class, column = "orderId",
                    many = @Many(select = "findOrderItemsByOrderId"))
    })
    List<Orders> findOrdersWithItems(Long userId);

    // 二쇰Ц蹂� �븘�씠�뀥 議고쉶
    @Select("SELECT oi.order_item_id AS orderItemId, oi.order_id AS orderId, oi.book_id AS bookId, oi.quantity AS quantity, oi.price AS price, " +
            "b.book_id AS bookId, b.title AS title " +
            "FROM order_items oi JOIN books b ON oi.book_id = b.book_id WHERE oi.order_id = #{orderId}")
    @Results(id="OrderItemWithBook", value = {
            @Result(property="orderItemId", column="orderItemId"),
            @Result(property="orderId", column="orderId"),
            @Result(property="bookId", column="bookId"),
            @Result(property="quantity", column="quantity"),
            @Result(property="price", column="price"),
            @Result(property="book.bookId", column="bookId"),
            @Result(property="book.title", column="title")
    })
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

   

    

    // �윋� 理쒓렐 二쇰Ц n媛�
    @Select("SELECT order_id AS orderId, user_id AS userId, order_date AS orderDate, total_amount AS totalAmount, status AS status " +
            "FROM (SELECT * FROM orders ORDER BY order_date DESC) WHERE ROWNUM <= #{limit}")
    List<Orders> findRecentOrders(@Param("limit") int limit);
 // ✅ 오늘 주문 건수
    @Select("SELECT COUNT(*) FROM orders WHERE TRUNC(order_date)=TRUNC(SYSDATE)")
    int countTodayOrders();

    // ✅ 오늘 매출(취소 제외)
    @Select("SELECT NVL(SUM(total_amount),0) FROM orders " +
            "WHERE TRUNC(order_date)=TRUNC(SYSDATE) AND NVL(status,'PAID') <> 'CANCELLED'")
    Integer sumTodayRevenue();

    // ✅ 최근 주문 n개 (대시보드 표)
    @Select(
      "SELECT * FROM ( " +
      "  SELECT o.order_id AS orderId, o.user_id AS userId, o.total_amount AS totalAmount, " +
      "         o.status AS status, o.order_date AS orderDate " +
      "  FROM orders o ORDER BY o.order_date DESC " +
      ") WHERE ROWNUM <= #{limit}"
    )
    List<Orders> findRecent(@Param("limit") int limit);

    // ✅ 최근 N개월 월별 매출(취소 제외) — 라벨은 'MM월'
    @Select(
      "SELECT TO_CHAR(TRUNC(order_date,'MM'),'MM\"월\"') AS label, " +
      "       SUM(total_amount) AS value, " +
      "       TRUNC(order_date,'MM') AS mon " +
      "FROM orders " +
      "WHERE order_date >= ADD_MONTHS(TRUNC(SYSDATE,'MM'), - (#{months}-1)) " +
      "  AND NVL(status,'PAID') <> 'CANCELLED' " +
      "GROUP BY TRUNC(order_date,'MM') " +
      "ORDER BY mon"
    )
    List<Map<String,Object>> monthlyRevenue(@Param("months") int months);

    // ✅ 주문 상태 분포 (막대/도넛 등)
    @Select("SELECT NVL(status,'PAID') AS label, COUNT(*) AS value " +
            "FROM orders GROUP BY NVL(status,'PAID')")
    List<Map<String,Object>> statusCounts();
}