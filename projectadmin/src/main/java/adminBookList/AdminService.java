package adminBookList;

import java.util.List;

public interface AdminService{
		
		public void addBook(Book book);
		public void updateBook(Book book);
		void deleteBook(int bookId);
		public List<Book> getAllBooks();
		 
	}

