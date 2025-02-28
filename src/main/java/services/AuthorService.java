package services;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import dao.AuthorDao;
import dao.AuthorDaoImpl;
import dao.LiteraryGenreDao;
import dao.LiteraryGenreDaoImpl;
import utils.SelectOptions;
import models.Author;
import models.LiteraryGenre;

public class AuthorService {

    private AuthorDao authorDao = new AuthorDaoImpl();
    private LiteraryGenreDao literaryGenreDao = new LiteraryGenreDaoImpl();

    public List<Author> listAuthors() throws Exception {
        List<Author> authorData = authorDao.listAuthors();
        for (Author author : authorData) {
            byte[] photo = author.getPhoto();
            if (photo != null) {
                String photoBase64 = Base64.getEncoder().encodeToString(photo);
                author.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
            }
        }
        return authorData;
    }
    
    public Author getAuthor(String authorId) throws Exception {
        Author author = authorDao.getAuthor(authorId);
        byte[] photo = author.getPhoto();
        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            author.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
        }
        return author;
    }
    
    public Author createAuthor(HttpServletRequest request) throws Exception {
        String name = request.getParameter("addAuthorName");
        String nationality = request.getParameter("addAuthorNationality");
        String literaryGenreId = request.getParameter("addLiteraryGenre");
        LocalDate birthDate = LocalDate.parse(request.getParameter("addAuthorBirthDate"));
        String biography = request.getParameter("addAuthorBiography");
        String status = request.getParameter("addAuthorStatus");
        
        byte[] photo = null;
        try {
            InputStream inputStream = request.getPart("addAuthorPhoto").getInputStream();
            if (inputStream.available() > 0) {
                photo = inputStream.readAllBytes();
            }
        } catch (Exception e) {
        }
        
        Author author = new Author();
        author.setName(name);
        author.setNationality(nationality);
        author.setLiteraryGenreId(literaryGenreId);
        author.setBirthDate(birthDate);
        author.setBiography(biography);
        author.setStatus(status);
        author.setPhoto(photo);
        
        Author createdAuthor = authorDao.createAuthor(author);
        if (createdAuthor.getPhoto() != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(createdAuthor.getPhoto());
            createdAuthor.setPhotoBase64("data:image/jpeg;base64," + photoBase64);
        }
        return createdAuthor;
    }
    
    public Author updateAuthor(HttpServletRequest request) throws Exception {
        String authorId = request.getParameter("authorId");
        String name = request.getParameter("editAuthorName");
        String nationality = request.getParameter("editAuthorNationality");
        String literaryGenreId = request.getParameter("editLiteraryGenre");
        LocalDate birthDate = LocalDate.parse(request.getParameter("editAuthorBirthDate"));
        String biography = request.getParameter("editAuthorBiography");
        String status = request.getParameter("editAuthorStatus");
        
        byte[] photo = null;
        try {
            InputStream inputStream = request.getPart("editAuthorPhoto").getInputStream();
            if (inputStream.available() > 0) {
            	photo = inputStream.readAllBytes();
            }
        } catch (Exception e) {
        }
        if (photo == null) {
            Author currentAuthor = authorDao.getAuthor(authorId);
            photo = currentAuthor.getPhoto();
        }
        
        Author author = new Author();
        author.setAuthorId(authorId);
        author.setName(name);
        author.setNationality(nationality);
        author.setLiteraryGenreId(literaryGenreId);
        author.setBirthDate(birthDate);
        author.setBiography(biography);
        author.setStatus(status);
        author.setPhoto(photo);
        
        return authorDao.updateAuthor(author);
    }
    
    public SelectOptions populateSelects() throws Exception {
        SelectOptions selectOptions = new SelectOptions();
        
        List<LiteraryGenre> literaryGenres = literaryGenreDao.populateLiteraryGenreSelect();
        selectOptions.setLiteraryGenres(literaryGenres);
        
        return selectOptions;
    }
}
