package bookList;

import domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookListRepository {

    // 전체 데이터 조회 (기존)
    @Select("SELECT book_id AS bookId, title, author, price, cover_image AS coverImage " +
            "FROM books ORDER BY book_id DESC")
    List<Book> findAll();

    // 검색 데이터 조회 (기존)
    @Select("SELECT book_id AS bookId, title, author, price, cover_image AS coverImage " +
            "FROM books " +
            "WHERE LOWER(title) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%' " +
            "   OR LOWER(author) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%' " +
            "ORDER BY book_id DESC")
    List<Book> findByKeyword(@Param("keyword") String keyword);

    // 페이징용 전체 개수 조회 (수정)
    @Select("SELECT COUNT(*) FROM books " +
            "WHERE (#{keyword, jdbcType=VARCHAR} IS NULL OR #{keyword, jdbcType=VARCHAR} = '' " +
            "   OR LOWER(title) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%' " +
            "   OR LOWER(author) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%')")
    int countByKeyword(@Param("keyword") String keyword);

    // 페이징 데이터 조회 (수정)
    @Select(
        "SELECT * FROM ( " +
        "  SELECT b.*, ROWNUM rnum FROM ( " +
        "    SELECT book_id AS bookId, title, author, price, cover_image AS coverImage " +
        "    FROM books " +
        "    WHERE (#{keyword, jdbcType=VARCHAR} IS NULL OR #{keyword, jdbcType=VARCHAR} = '' " +
        "       OR LOWER(title) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%' " +
        "       OR LOWER(author) LIKE '%'||LOWER(#{keyword, jdbcType=VARCHAR})||'%') " +
        "    ORDER BY book_id DESC " +
        "  ) b WHERE ROWNUM <= #{endRow} " +
        ") WHERE rnum > #{startRow}"
    )
    List<Book> findPageByKeyword(
        @Param("keyword") String keyword,
        @Param("startRow") int startRow,
        @Param("endRow") int endRow
    );

    // (기존) cover_image BLOB만 조회
    @Select("SELECT cover_image FROM books WHERE book_id = #{bookId}")
    byte[] findCoverImageById(Long bookId);
}
