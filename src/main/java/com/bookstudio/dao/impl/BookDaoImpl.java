package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.BookDao;
import com.bookstudio.models.Book;
import com.bookstudio.utils.DbConnection;

public class BookDaoImpl implements BookDao {
	@Override
	public List<Book> listBooks() {
		List<Book> bookList = new ArrayList<>();

		String sql = """
				    SELECT
				        b.BookID, b.Title, b.TotalCopies, b.LoanedCopies,
				        b.AuthorID, a.Name AS AuthorName,
				        b.PublisherID, p.Name AS PublisherName,
				        b.CourseID, c.Name AS CourseName,
				        b.ReleaseDate,
				        b.GenreID, g.GenreName AS GenreName,
				        b.Status
				    FROM
				        Books b
				    JOIN
				        Authors a ON b.AuthorID = a.AuthorID
				    JOIN
				        Publishers p ON b.PublisherID = p.PublisherID
				    JOIN
				        Courses c ON b.CourseID = c.CourseID
				    JOIN
				        Genres g ON b.GenreID = g.GenreID
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Book book = new Book();
				book.setBookId(rs.getString("BookID"));
				book.setTitle(rs.getString("Title"));
				book.setTotalCopies(rs.getInt("TotalCopies"));
				int availableCopies = rs.getInt("TotalCopies") - rs.getInt("LoanedCopies");
				book.setAvailableCopies(availableCopies);
				book.setLoanedCopies(rs.getInt("LoanedCopies"));
				book.setAuthorId(rs.getString("AuthorID"));
				book.setAuthorName(rs.getString("AuthorName"));
				book.setPublisherId(rs.getString("PublisherID"));
				book.setPublisherName(rs.getString("PublisherName"));
				book.setCourseId(rs.getString("CourseID"));
				book.setCourseName(rs.getString("CourseName"));
				book.setReleaseDate(rs.getDate("ReleaseDate").toLocalDate());
				book.setGenreId(rs.getString("GenreID"));
				book.setGenreName(rs.getString("GenreName"));
				book.setStatus(rs.getString("Status"));
				bookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookList;
	}

	@Override
	public Book getBook(String idLibro) {
		Book book = null;

		String sql = """
				    SELECT
				        b.BookID, b.Title, b.TotalCopies, b.LoanedCopies,
				        b.AuthorID, a.Name AS AuthorName,
				        b.PublisherID, p.Name AS PublisherName,
				        b.CourseID, c.Name AS CourseName,
				        b.ReleaseDate,
				        b.GenreID, g.GenreName AS GenreName,
				        b.Status
				    FROM
				        Books b
				    JOIN
				        Authors a ON b.AuthorID = a.AuthorID
				    JOIN
				        Publishers p ON b.PublisherID = p.PublisherID
				    JOIN
				        Courses c ON b.CourseID = c.CourseID
				    JOIN
				        Genres g ON b.GenreID = g.GenreID
				    WHERE
				        b.BookID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, idLibro);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					book = new Book();
					book.setBookId(rs.getString("BookID"));
					book.setTitle(rs.getString("Title"));
					book.setTotalCopies(rs.getInt("TotalCopies"));
					int availableCopies = rs.getInt("TotalCopies") - rs.getInt("LoanedCopies");
					book.setAvailableCopies(availableCopies);
					book.setLoanedCopies(rs.getInt("LoanedCopies"));
					book.setAuthorId(rs.getString("AuthorID"));
					book.setAuthorName(rs.getString("AuthorName"));
					book.setPublisherId(rs.getString("PublisherID"));
					book.setPublisherName(rs.getString("PublisherName"));
					book.setCourseId(rs.getString("CourseID"));
					book.setCourseName(rs.getString("CourseName"));
					book.setReleaseDate(rs.getDate("ReleaseDate").toLocalDate());
					book.setGenreId(rs.getString("GenreID"));
					book.setGenreName(rs.getString("GenreName"));
					book.setStatus(rs.getString("Status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return book;
	}

	@Override
	public Book createBook(Book book) {
		String sqlInsert = """
				    INSERT INTO Books (Title, TotalCopies, LoanedCopies, AuthorID, PublisherID, CourseID, ReleaseDate, GenreID, Status)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		String sqlSelect = """
				    SELECT
				        b.TotalCopies, b.LoanedCopies,
				        a.Name AS AuthorName,
				        p.Name AS PublisherName,
				        c.Name AS CourseName
				    FROM
				        Books b
				    INNER JOIN
				        Authors a ON b.AuthorID = a.AuthorID
				    INNER JOIN
				        Publishers p ON b.PublisherID = p.PublisherID
				    INNER JOIN
				        Courses c ON b.CourseID = c.CourseID
				    WHERE
				        b.BookID = ?
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement psInsert = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {

			psInsert.setString(1, book.getTitle());
			psInsert.setInt(2, book.getTotalCopies());
			psInsert.setInt(3, book.getLoanedCopies());
			psInsert.setString(4, book.getAuthorId());
			psInsert.setString(5, book.getPublisherId());
			psInsert.setString(6, book.getCourseId());
			psInsert.setDate(7, java.sql.Date.valueOf(book.getReleaseDate()));
			psInsert.setString(8, book.getGenreId());
			psInsert.setString(9, book.getStatus());

			int rowsAffected = psInsert.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
					if (rsKeys.next()) {
						book.setBookId(rsKeys.getString(1));
					}
				}
				
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, book.getBookId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							int availableCopies = rs.getInt("TotalCopies") - rs.getInt("LoanedCopies");
							book.setAvailableCopies(availableCopies);
							book.setAuthorName(rs.getString("AuthorName"));
							book.setPublisherName(rs.getString("PublisherName"));
							book.setCourseName(rs.getString("CourseName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}

	@Override
	public Book updateBook(Book book) {
		String sqlUpdate = """
				    UPDATE Books
				    SET Title = ?, TotalCopies = ?,
				        AuthorID = ?, PublisherID = ?, CourseID = ?,
				        ReleaseDate = ?, GenreID = ?, Status = ?
				    WHERE BookID = ?
				""";

		String sqlSelect = """
				    SELECT
				        b.TotalCopies, b.LoanedCopies,
				        a.Name AS AuthorName,
				        p.Name AS PublisherName,
				        c.Name AS CourseName
				    FROM
				        Books b
				    INNER JOIN
				        Authors a ON b.AuthorID = a.AuthorID
				    INNER JOIN
				        Publishers p ON b.PublisherID = p.PublisherID
				    INNER JOIN
				        Courses c ON b.CourseID = c.CourseID
				    WHERE
				        b.BookID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {

			psUpdate.setString(1, book.getTitle());
			psUpdate.setInt(2, book.getTotalCopies());
			psUpdate.setString(3, book.getAuthorId());
			psUpdate.setString(4, book.getPublisherId());
			psUpdate.setString(5, book.getCourseId());
			psUpdate.setDate(6, java.sql.Date.valueOf(book.getReleaseDate()));
			psUpdate.setString(7, book.getGenreId());
			psUpdate.setString(8, book.getStatus());
			psUpdate.setString(9, book.getBookId());

			int rowsUpdated = psUpdate.executeUpdate();
			if (rowsUpdated > 0) {
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, book.getBookId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							book.setLoanedCopies(rs.getInt("LoanedCopies"));
							int availableCopies = rs.getInt("TotalCopies") - rs.getInt("LoanedCopies");
							book.setAvailableCopies(availableCopies);
							book.setAuthorName(rs.getString("AuthorName"));
							book.setPublisherName(rs.getString("PublisherName"));
							book.setCourseName(rs.getString("CourseName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return book;
	}

	@Override
	public List<Book> populateBookSelect() {
		List<Book> bookList = new ArrayList<>();

		String sql = """
				    SELECT
				        BookID, Title,
				        TotalCopies - LoanedCopies AS AvailableCopies
				    FROM
				        Books
				    WHERE
				        Status = 'activo'
				        AND TotalCopies > LoanedCopies
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Book book = new Book();
				book.setBookId(rs.getString("BookID"));
				book.setTitle(rs.getString("Title"));
				book.setAvailableCopies(rs.getInt("AvailableCopies"));
				bookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookList;
	}
}
