package adminBookDetail;

import domain.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminBookDetailRepository {
    Book selectBookById(int bookId);
}