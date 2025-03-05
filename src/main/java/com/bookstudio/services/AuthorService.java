package com.bookstudio.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.dao.AuthorDao;
import com.bookstudio.dao.LiteraryGenreDao;
import com.bookstudio.dao.impl.AuthorDaoImpl;
import com.bookstudio.dao.impl.LiteraryGenreDaoImpl;
import com.bookstudio.models.Author;
import com.bookstudio.models.LiteraryGenre;
import com.bookstudio.utils.SelectOptions;

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
    	String name = getUtf8Parameter(request, "addAuthorName");
        String nationality = getUtf8Parameter(request, "addAuthorNationality");
        String literaryGenreId = getUtf8Parameter(request, "addLiteraryGenre");
        LocalDate birthDate = LocalDate.parse(request.getParameter("addAuthorBirthDate"));
        String biography = getUtf8Parameter(request, "addAuthorBiography");
        String status = getUtf8Parameter(request, "addAuthorStatus");
        
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
        String name = getUtf8Parameter(request, "editAuthorName");
        String nationality = getUtf8Parameter(request, "editAuthorNationality");
        String literaryGenreId = getUtf8Parameter(request, "editLiteraryGenre");
        LocalDate birthDate = LocalDate.parse(request.getParameter("editAuthorBirthDate"));
        String biography = getUtf8Parameter(request, "editAuthorBiography");
        String status = getUtf8Parameter(request, "editAuthorStatus");
        
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
    
    private String getUtf8Parameter(HttpServletRequest request, String fieldName) throws IOException, ServletException {
        Part part = request.getPart(fieldName);
        if (part != null) {
            try (InputStream is = part.getInputStream()) {
                return new String(is.readAllBytes(), "UTF-8");
            }
        }
        
		return "";
	}
}
