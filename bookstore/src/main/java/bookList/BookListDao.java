package bookList;

import domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BookListDao {
    @Select("SELECT book_id AS bookId, title, author, price, cover_image AS coverImage " +
            "FROM books ORDER BY updated_at DESC")
    List<Book> selectAll();

    @Select("SELECT book_id AS bookId, title, author, price, cover_image AS coverImage " +
            "FROM books " +
            "WHERE LOWER(title) LIKE '%'||LOWER(#{kw})||'%' " +
            "   OR LOWER(author) LIKE '%'||LOWER(#{kw})||'%' " +
            "ORDER BY updated_at DESC")
    List<Book> selectByKeyword(@Param("kw") String kw);
}
