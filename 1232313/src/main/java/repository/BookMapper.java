package repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import vo.Book;

@Mapper
public interface BookMapper {

    // 표지 필드는 DB 컬럼명 그대로: cover_image  (VO도 private String cover_image;)
    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
            "FROM books")
    List<Book> findAll();

    // 🔹 keyword가 null이어도 안전 (NVL로 빈문자 대체 + jdbcType=VARCHAR로 바인딩 오류 방지)
    @Select("SELECT COUNT(*) " +
            "FROM books " +
            "WHERE title LIKE '%' || NVL(#{keyword,jdbcType=VARCHAR}, '') || '%'")
    int countByKeyword(@Param("keyword") String keyword);

    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
            "FROM books " +
            "WHERE title LIKE '%' || NVL(#{keyword,jdbcType=VARCHAR}, '') || '%'")
    List<Book> findByKeyword(@Param("keyword") String keyword);

    // 🔹 페이징 (Oracle ROWNUM) + null-safe LIKE
    @Select(
        "SELECT * FROM ( " +
        "  SELECT a.*, ROWNUM rnum FROM ( " +
        "    SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
        "    FROM books " +
        "    WHERE title LIKE '%' || NVL(#{keyword,jdbcType=VARCHAR}, '') || '%' " +
        "    ORDER BY book_id " +
        "  ) a WHERE ROWNUM <= #{startRow} + #{pageSize} " +
        ") WHERE rnum > #{startRow}"
    )
    List<Book> findPageByKeyword(
        @Param("keyword") String keyword,
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize
    );

    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
            "FROM books WHERE book_id = #{bookId}")
    Book selectBookById(@Param("bookId") Long bookId);

    @Update("UPDATE books SET stock = #{stock} WHERE book_id = #{bookId}")
    void updateBookStock(Book book);

    // 전체 도서 수 (대시보드 KPI)
    @Select("SELECT COUNT(*) FROM books")
    int countAll();

    // 재고 임박 도서
    @Select(
      "SELECT * FROM ( " +
      "  SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
      "  FROM books " +
      "  WHERE stock <= #{threshold} " +
      "  ORDER BY stock ASC " +
      ") WHERE ROWNUM <= #{limit}"
    )
    List<Book> findLowStock(@Param("threshold") int threshold, @Param("limit") int limit);

    // 카테고리 분포 (category가 NULL이면 '기타'로) — 컬럼 라벨을 확정
    @Select(
      "SELECT NVL(category,'기타') AS CATEGORY, COUNT(*) AS TOTAL " +
      "FROM books " +
      "GROUP BY NVL(category,'기타') " +
      "ORDER BY TOTAL DESC"
    )
    List<Map<String,Object>> categoryCounts();
}