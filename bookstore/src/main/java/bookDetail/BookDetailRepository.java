package bookDetail;

import domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BookDetailRepository {

    @Select({
      "SELECT",
      "  book_id       AS bookId,",
      "  title,",
      "  author,",
      "  price,",
      "  stock,",
      "  description,",
      "  cover_image   AS coverImage",
      "FROM books",
      "WHERE book_id = #{bookId}"
    })
    Book findById(Long bookId);

    /** 상세보기 페이지에서 이미지만 별도로 로드할 때 */
    @Select("SELECT cover_image FROM books WHERE book_id = #{bookId}")
    byte[] findCoverImageById(Long bookId);
}
