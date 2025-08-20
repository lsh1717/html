package repository;

import java.util.List;
import org.apache.ibatis.annotations.*;

import vo.OrderItem;
import vo.Orders;

@Mapper
public interface OrderMapper {

    // 주문 생성
    @Insert("INSERT INTO orders (order_id, user_id, order_date, total_amount, status) " +
            "VALUES (SEQ_ORDERS.NEXTVAL, #{userId}, SYSDATE, #{totalAmount}, #{status})")
    @SelectKey(statement = "SELECT SEQ_ORDERS.CURRVAL FROM dual", keyProperty = "orderId", before = false, resultType = long.class)
    void insertOrder(Orders order);

    // 주문 아이템 생성
    @Insert("INSERT INTO order_items (order_item_id, order_id, book_id, quantity, price) " +
            "VALUES (SEQ_ORDER_ITEMS.NEXTVAL, #{orderId}, #{bookId}, #{quantity}, #{price})")
    @SelectKey(statement = "SELECT SEQ_ORDER_ITEMS.CURRVAL FROM dual", keyProperty = "orderItemId", before = false, resultType = long.class)
    void insertOrderItem(OrderItem orderItem);

    // 회원별 주문내역
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

    // 주문별 아이템 조회
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

    // 📊 오늘 주문 수
    @Select("SELECT COUNT(*) FROM orders WHERE TRUNC(order_date) = TRUNC(SYSDATE)")
    int countTodayOrders();

    // 📊 오늘 매출 합계
    @Select("SELECT NVL(SUM(total_amount),0) FROM orders WHERE TRUNC(order_date) = TRUNC(SYSDATE)")
    int sumTodayRevenue();

    // 📊 최근 주문 n개
    @Select("SELECT order_id AS orderId, user_id AS userId, order_date AS orderDate, total_amount AS totalAmount, status AS status " +
            "FROM (SELECT * FROM orders ORDER BY order_date DESC) WHERE ROWNUM <= #{limit}")
    List<Orders> findRecentOrders(@Param("limit") int limit);
}