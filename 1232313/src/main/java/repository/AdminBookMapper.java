package repository;

import java.util.List;
import org.apache.ibatis.annotations.*;
import vo.Book;

@Mapper
public interface AdminBookMapper {

    // 총 개수 (제목/저자 검색)
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM books",
        "<where>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    (LOWER(title) LIKE '%'||LOWER(#{keyword})||'%'",
        "     OR LOWER(author) LIKE '%'||LOWER(#{keyword})||'%')",
        "  </if>",
        "</where>",
        "</script>"
    })
    int count(@Param("keyword") String keyword);

    // 페이지 조회 (최신 book_id DESC)
    @Select({
        "<script>",
        "SELECT book_id AS bookId, title, author, description, price, stock, cover_image",
        "FROM books",
        "<where>",
        "  <if test='keyword != null and keyword.trim() != \"\"'>",
        "    (LOWER(title) LIKE '%'||LOWER(#{keyword})||'%'\n",
        "     OR LOWER(author) LIKE '%'||LOWER(#{keyword})||'%')",
        "  </if>",
        "</where>",
        "ORDER BY book_id DESC",
        "OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY",
        "</script>"
    })
    List<Book> findPaged(@Param("keyword") String keyword,
                         @Param("offset") int offset,
                         @Param("pageSize") int pageSize);

    // 단건 조회
    @Select("SELECT book_id AS bookId, title, author, description, price, stock, cover_image FROM books WHERE book_id=#{id}")
    Book findById(@Param("id") Long id);

    // 등록
    @Insert("INSERT INTO books (book_id, title, author, description, price, stock, cover_image) " +
            "VALUES (SEQ_BOOKS.NEXTVAL, #{title}, #{author}, #{description}, #{price}, #{stock}, #{cover_image})")
    @SelectKey(statement = "SELECT SEQ_BOOKS.CURRVAL FROM dual", keyProperty = "bookId", before = false, resultType = long.class)
    int insert(Book b);

    // 수정
    @Update("UPDATE books SET title=#{title}, author=#{author}, description=#{description}, price=#{price}, stock=#{stock}, cover_image=#{cover_image} WHERE book_id=#{bookId}")
    int update(Book b);

    // 삭제
    @Delete("DELETE FROM books WHERE book_id=#{id}")
    int delete(@Param("id") Long id);
}