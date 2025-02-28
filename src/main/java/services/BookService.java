package services;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import dao.BookDao;
import dao.BookDaoImpl;
import dao.AuthorDao;
import dao.AuthorDaoImpl;
import dao.PublisherDao;
import dao.PublisherDaoImpl;
import dao.CourseDao;
import dao.CourseDaoImpl;
import dao.GenreDao;
import dao.GenreDaoImpl;
import models.Book;
import models.Author;
import models.Publisher;
import models.Course;
import models.Genre;
import utils.SelectOptions;

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
