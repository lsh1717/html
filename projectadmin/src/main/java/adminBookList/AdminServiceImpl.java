package adminBookList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Book;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void addBook(Book book) {
        adminMapper.insertBook(book);
    }

    @Override
    public void updateBook(Book book) {
        adminMapper.updateBook(book);
    }

    @Override
    public void deleteBook(int bookId) {
        adminMapper.deleteBook(bookId);
    }

	@Override
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		return null;
	}
}