package repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import vo.Orders;
import vo.OrderItem;

@Mapper
public interface OrderMapper {

    // 1️⃣ 주문 기록(Orders) 추가
    @Insert("INSERT INTO orders (member_id, order_date) VALUES (#{memberId}, SYSDATE)")
    @Options(useGeneratedKeys = true, keyProperty = "orderId") // 생성된 PK를 orderId에 매핑
    void insertOrder(Orders order);

    // 2️⃣ 주문 항목(OrderItem) 추가
    @Insert("INSERT INTO order_items (order_id, book_id, quantity, price) " +
            "VALUES (#{orderId}, #{bookId}, #{quantity}, #{price})")
    void insertOrderItem(OrderItem orderItem);

}