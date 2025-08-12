package repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import vo.Book;

@Mapper
public interface BookMapper {

    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image FROM books")
    List<Book> findAll();

    @Select("SELECT COUNT(*) FROM books WHERE title LIKE '%' || #{keyword,jdbcType=VARCHAR} || '%'")
    int countByKeyword(@Param("keyword") String keyword);

    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
            "FROM books WHERE title LIKE '%' || #{keyword,jdbcType=VARCHAR} || '%'")
    List<Book> findByKeyword(@Param("keyword") String keyword);

    @Select("SELECT * FROM ( " +
            "SELECT a.*, ROWNUM rnum FROM ( " +
            "SELECT book_id AS bookId, title, author, description, price, stock, cover_image " +
            "FROM books WHERE title LIKE '%' || #{keyword,jdbcType=VARCHAR} || '%' ORDER BY book_id " +
            ") a WHERE ROWNUM <= #{startRow} + #{pageSize} " +
            ") WHERE rnum > #{startRow}")
    List<Book> findPageByKeyword(
        @Param("keyword") String keyword,
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize
    );

    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image FROM books WHERE book_id = #{bookId}")
    Book selectBookById(@Param("bookId") Long bookId);
}