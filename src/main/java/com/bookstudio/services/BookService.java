package com.bookstudio.services;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.dao.AuthorDao;
import com.bookstudio.dao.BookDao;
import com.bookstudio.dao.CourseDao;
import com.bookstudio.dao.GenreDao;
import com.bookstudio.dao.PublisherDao;
import com.bookstudio.dao.impl.AuthorDaoImpl;
import com.bookstudio.dao.impl.BookDaoImpl;
import com.bookstudio.dao.impl.CourseDaoImpl;
import com.bookstudio.dao.impl.GenreDaoImpl;
import com.bookstudio.dao.impl.PublisherDaoImpl;
import com.bookstudio.models.Author;
import com.bookstudio.models.Book;
import com.bookstudio.models.Course;
import com.bookstudio.models.Genre;
import com.bookstudio.models.Publisher;
import com.bookstudio.utils.SelectOptions;

public class BookService {
	private BookDao bookDao = new BookDaoImpl();
	private AuthorDao authorDao = new AuthorDaoImpl();
	private PublisherDao publisherDao = new PublisherDaoImpl();
	private CourseDao courseDao = new CourseDaoImpl();
	private GenreDao genreDao = new GenreDaoImpl();

	public List<Book> listBooks() throws Exception {
		return bookDao.listBooks();
	}

	public Book getBook(String bookId) throws Exception {
		return bookDao.getBook(bookId);
	}

	public Book createBook(HttpServletRequest request) throws Exception {
		String title = request.getParameter("addBookTitle");
		int totalCopies = Integer.parseInt(request.getParameter("addBookTotalCopies"));
		String authorId = request.getParameter("addBookAuthor");
		String publisherId = request.getParameter("addBookPublisher");
		String courseId = request.getParameter("addBookCourse");
		LocalDate releaseDate = LocalDate.parse(request.getParameter("addReleaseDate"));
		String genreId = request.getParameter("addBookGenre");
		String status = request.getParameter("addBookStatus");

		Book book = new Book();
		book.setTitle(title);
		book.setTotalCopies(totalCopies);
		book.setAuthorId(authorId);
		book.setPublisherId(publisherId);
		book.setCourseId(courseId);
		book.setReleaseDate(releaseDate);
		book.setGenreId(genreId);
		book.setStatus(status);

		return bookDao.createBook(book);
	}

	public Book updateBook(HttpServletRequest request) throws Exception {
		String bookId = request.getParameter("bookId");
		String title = request.getParameter("editBookTitle");
		int totalCopies = Integer.parseInt(request.getParameter("editBookTotalCopies"));
		String authorId = request.getParameter("editBookAuthor");
		String publisherId = request.getParameter("editBookPublisher");
		String courseId = request.getParameter("editBookCourse");
		LocalDate releaseDate = LocalDate.parse(request.getParameter("editReleaseDate"));
		String genreId = request.getParameter("editBookGenre");
		String status = request.getParameter("editBookStatus");

		Book book = new Book();
		book.setBookId(bookId);
		book.setTitle(title);
		book.setTotalCopies(totalCopies);
		book.setAuthorId(authorId);
		book.setPublisherId(publisherId);
		book.setCourseId(courseId);
		book.setReleaseDate(releaseDate);
		book.setGenreId(genreId);
		book.setStatus(status);

		return bookDao.updateBook(book);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		List<Author> authors = authorDao.populateAuthorSelect();
		selectOptions.setAuthors(authors);

		List<Publisher> publishers = publisherDao.populatePublisherSelect();
		selectOptions.setPublishers(publishers);

		List<Course> courses = courseDao.populateCourseSelect();
		selectOptions.setCourses(courses);

		List<Genre> genres = genreDao.populateGenreSelect();
		selectOptions.setGenres(genres);

		return selectOptions;
	}
}
