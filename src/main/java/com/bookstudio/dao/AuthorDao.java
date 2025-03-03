package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Author;

public interface AuthorDao {
	public List<Author> listAuthors();
	public Author getAuthor(String authorId);
	public Author createAuthor(Author author);
	public Author updateAuthor(Author author);
	public List<Author> populateAuthorSelect();
}
