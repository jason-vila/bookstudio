package dao;

import java.util.List;

import models.Author;

public interface AuthorDao {
	public List<Author> listAuthors();
	public Author getAuthor(String authorId);
	public Author createAuthor(Author author);
	public Author updateAuthor(Author author);
	public List<Author> populateAuthorSelect();
}
