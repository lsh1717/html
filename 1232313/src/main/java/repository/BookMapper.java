package repository;

import java.util.List;
import org.apache.ibatis.annotations.*;
import vo.Book;

@Mapper
public interface BookMapper {

    // 전체
    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image FROM books")
    List<Book> findAll();

    // 총 건수 (keyword 없으면 WHERE 제외)
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM books",
        "<where>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    (LOWER(title) LIKE '%' || LOWER(#{keyword}) || '%' ",
        "     OR LOWER(author) LIKE '%' || LOWER(#{keyword}) || '%')",
        "  </if>",
        "</where>",
        "</script>"
    })
    int countByKeyword(@Param("keyword") String keyword);

    // 키워드 목록
    @Select({
        "<script>",
        "SELECT book_id AS bookId, title, author, description, price, stock, cover_image",
        "FROM books",
        "<where>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    (LOWER(title) LIKE '%' || LOWER(#{keyword}) || '%' ",
        "     OR LOWER(author) LIKE '%' || LOWER(#{keyword}) || '%')",
        "  </if>",
        "</where>",
        "ORDER BY book_id DESC",
        "</script>"
    })
    List<Book> findByKeyword(@Param("keyword") String keyword);

    // Oracle 페이징 (ROWNUM) — endRow 사용
    @Select({
        "<script>",
        "SELECT * FROM (",
        "  SELECT a.*, ROWNUM rn FROM (",
        "    SELECT book_id AS bookId, title, author, description, price, stock, cover_image",
        "    FROM books",
        "    <where>",
        "      <if test='keyword != null and keyword.trim() != \"\"'>",
        "        (LOWER(title) LIKE '%' || LOWER(#{keyword}) || '%' ",
        "         OR LOWER(author) LIKE '%' || LOWER(#{keyword}) || '%')",
        "      </if>",
        "    </where>",
        "    ORDER BY book_id DESC",
        "  ) a",
        "  WHERE ROWNUM &lt;= #{endRow}",
        ") WHERE rn &gt; #{startRow}",
        "</script>"
    })
    List<Book> findPageByKeyword(
        @Param("keyword") String keyword,
        @Param("startRow") int startRow,
        @Param("endRow") int endRow
    );

    // 단건
    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image FROM books WHERE book_id = #{bookId}")
    Book selectBookById(@Param("bookId") Long bookId);

    @Update("UPDATE books SET stock = #{stock} WHERE book_id = #{bookId}")
    void updateBookStock(Book book);

    @Select("SELECT COUNT(*) FROM books")
    int countAll();

    // ✅ 재고 임박 (여기가 문제였음: <= 를 HTML 엔티티로 쓰지 말 것)
    @Select(
        "SELECT * FROM (" +
        "  SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
        "  FROM books " +
        "  WHERE stock <= #{threshold} " +        // ← 진짜 <=
        "  ORDER BY stock ASC" +
        ") WHERE ROWNUM <= #{limit}"              // ← 진짜 <=
    )
    List<Book> findLowStock(@Param("threshold") int threshold, @Param("limit") int limit);

 // 최근 {days}일 베스트셀러 {limit}권 (판매 0권 제외, 관리자 TOP5와 동일 기준)
    @Select({
      "<script>",
      "SELECT * FROM (",
      "  SELECT",
      "    b.book_id     AS bookId,",
      "    b.title       AS title,",
      "    b.author      AS author,",
      "    b.description AS description,",
      "    b.price       AS price,",
      "    b.stock       AS stock,",
      "    b.cover_image AS cover_image,",
      "    SUM(oi.quantity) AS totalQty",
      "  FROM orders o",
      "  JOIN order_items oi ON oi.order_id = o.id",               // ✅ 관리자와 동일: o.id
      "  JOIN books b        ON b.book_id  = oi.book_id",
      "  WHERE o.created_at &gt;= TRUNC(SYSDATE) - #{days}",       // ✅ 관리자와 동일: created_at
      // 주문 상태 컬럼이 있으면 취소 제외, 없으면 이 줄 삭제
      "    AND (o.status IS NULL OR o.status &lt;&gt; 'CANCELLED')",
      "  GROUP BY b.book_id, b.title, b.author, b.description, b.price, b.stock, b.cover_image",
      "  HAVING SUM(oi.quantity) > 0",                              // ✅ 0권 제외
      "  ORDER BY totalQty DESC, b.book_id DESC",
      ") WHERE ROWNUM &lt;= #{limit}",
      "</script>"
    })
    List<Book> findBestSellers(@Param("days") int days, @Param("limit") int limit);
    
    
    
    @Select({
    	  "<script>",
    	  "SELECT * FROM (",
    	  "  SELECT book_id AS bookId, title, author, description, price, stock, cover_image",
    	  "  FROM books",
    	  "  WHERE stock &gt; 0",
    	  "  <if test='excludeIds != null and excludeIds.size() > 0'>",
    	  "    AND book_id NOT IN",
    	  "    <foreach item='id' collection='excludeIds' open='(' separator=',' close=')'>",
    	  "      #{id}",
    	  "    </foreach>",
    	  "  </if>",
    	  "  ORDER BY DBMS_RANDOM.VALUE",
    	  ") WHERE ROWNUM &lt;= #{n}",
    	  "</script>"
    	})
    	List<Book> pickRandomActive(@Param("excludeIds") List<Long> excludeIds,
    	                            @Param("n") int n);
    
}