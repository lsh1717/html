package adminBookList;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

    // 도서 추가
    void insertBook(Book book);

    
    void updateBook(Book book);

    
    void deleteBook(int bookId);

}