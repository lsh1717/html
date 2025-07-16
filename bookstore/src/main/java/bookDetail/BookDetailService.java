package bookDetail;

import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookDetailService {
    @Autowired
    private BookDetailRepository repo;

    public Book getById(Long bookId) {
        return repo.findById(bookId);
    }

    public byte[] getCoverImage(Long bookId) {
        return repo.findCoverImageById(bookId);
    }
}
