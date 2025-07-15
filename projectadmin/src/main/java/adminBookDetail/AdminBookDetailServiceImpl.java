package adminBookDetail;

import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBookDetailServiceImpl implements AdminBookDetailService {

    @Autowired
    private AdminBookDetailRepository repository;

    @Override
    public Book getBookDetail(int bookId) {
        return repository.selectBookById(bookId);
    }
}