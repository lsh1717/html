package repository;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import vo.Book;
import java.util.Map;
@Mapper
public interface AdminBookMapper {

    /* 총 개수 (검색어 옵션) */
    @Lang(XMLLanguageDriver.class)
    @Select({
        "<script>",
        "SELECT COUNT(*)",
        "FROM books b",
        "<where>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    (LOWER(b.title)  LIKE '%' || LOWER(#{keyword}) || '%' ",
        "     OR LOWER(b.author) LIKE '%' || LOWER(#{keyword}) || '%')",
        "  </if>",
        "</where>",
        "</script>"
    })
    int count(@Param("keyword") String keyword);

    /* 목록 페이지 (Oracle 10g/11g 호환: ROW_NUMBER 페이징) */
    @Lang(XMLLanguageDriver.class)
    @Select({
        "<script>",
        "SELECT * FROM (",
        "  SELECT",
        "    b.book_id      AS bookId,",
        "    b.title        AS title,",
        "    b.author       AS author,",
        "    b.description  AS description,",
        "    b.price        AS price,",
        "    b.stock        AS stock,",
        "    b.cover_image  AS coverImage,",
        "    ROW_NUMBER() OVER (ORDER BY b.book_id DESC) AS rn",
        "  FROM books b",
        "  <where>",
        "    <if test='keyword != null and keyword.trim() != \"\"'>",
        "      (LOWER(b.title) LIKE '%' || LOWER(#{keyword}) || '%' ",
        "       OR LOWER(b.author) LIKE '%' || LOWER(#{keyword}) || '%')",
        "    </if>",
        "  </where>",
        ")",
        "WHERE rn BETWEEN #{start} AND #{end}",
        "</script>"
    })
    List<Book> findPaged(
        @Param("keyword") String keyword,
        @Param("start")   int start,
        @Param("end")     int end
    );

    /* 단건 조회 */
    @Select({
        "SELECT b.book_id AS bookId, b.title, b.author, b.description, b.price,",
        "       b.stock, b.cover_image AS coverImage",
        "  FROM books b",
        " WHERE b.book_id = #{id}"
    })
    Book findById(@Param("id") Long id);

    /* 등록 — 시퀀스 선 조회 후 사용 (before=true) */
    @Insert({
        "INSERT INTO books (book_id, title, author, description, price, stock, cover_image)",
        "VALUES (#{bookId}, #{title}, #{author}, #{description}, #{price}, #{stock}, #{coverImage})"
    })
    @SelectKey(
        statement = "SELECT SEQ_BOOKS.NEXTVAL FROM dual",
        keyProperty = "bookId",
        before = true,
        resultType = Long.class
    )
    int insert(Book b);

    /* 수정 */
    @Update({
        "UPDATE books",
        "   SET title       = #{title},",
        "       author      = #{author},",
        "       description = #{description},",
        "       price       = #{price},",
        "       stock       = #{stock},",
        "       cover_image = #{coverImage}",
        " WHERE book_id     = #{bookId}"
    })
    int update(Book b);

    /* 삭제 */
    @Delete("DELETE FROM books WHERE book_id = #{id}")
    int delete(@Param("id") Long id);
    
    @Select({
    	  "SELECT * FROM (",
    	  "  SELECT b.book_id AS bookId, b.title AS title,",
    	  "         SUM(oi.quantity) AS qty,",
    	  "         SUM(oi.quantity * oi.price) AS revenue",
    	  "    FROM orders o",
    	  "    JOIN order_items oi ON oi.order_id = o.order_id",
    	  "    JOIN books b        ON b.book_id = oi.book_id",
    	  "   WHERE o.order_date >= SYSDATE - #{days}",
    	  "     AND o.status <> 'CANCELLED'",
    	  "   GROUP BY b.book_id, b.title",
    	  "   ORDER BY qty DESC",
    	  ") WHERE ROWNUM <= #{limit}"
    	})
    	List<Map<String,Object>> topBooksByQty(@Param("days") int days, @Param("limit") int limit);
    
}