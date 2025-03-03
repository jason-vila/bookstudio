package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Book;

public interface BookDao {
	public List<Book> listBooks();
	public Book getBook(String bookId);
	public Book createBook(Book book);
	public Book updateBook(Book book);
	public List<Book> populateBookSelect();
}
