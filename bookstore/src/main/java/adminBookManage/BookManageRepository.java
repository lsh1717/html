package adminBookManage;

import java.util.List;

import domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface BookManageRepository {

    @Select("SELECT book_id AS bookId, title, author, price, stock, description, cover_image AS coverImage FROM books")
    List<Book> findAll();

    @Select("SELECT book_id AS bookId, title, author, price, stock, description, cover_image AS coverImage FROM books WHERE book_id = #{id}")
    Book findById(Long bookId);

    @Insert("INSERT INTO books (title, author, price, stock, description, cover_image) " +
            "VALUES (#{title}, #{author}, #{price}, #{stock}, #{description}, #{coverImage})")
    void insert(Book book);

    @Update("UPDATE books SET title=#{title}, author=#{author}, price=#{price}, stock=#{stock}, description=#{description}, cover_image=#{coverImage} WHERE book_id=#{bookId}")
    void update(Book book);

    @Delete("DELETE FROM books WHERE book_id = #{id}")
    void delete(Long bookId);
}