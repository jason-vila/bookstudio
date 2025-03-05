package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.AuthorDao;
import com.bookstudio.models.Author;
import com.bookstudio.utils.DbConnection;

public class AuthorDaoImpl implements AuthorDao {
	@Override
	public List<Author> listAuthors() {
		List<Author> authorList = new ArrayList<>();

		String sql = """
				    SELECT
				        a.AuthorID, a.Name, a.Nationality,
				        a.LiteraryGenreID, a.BirthDate,
				        a.Photo, a.Biography, a.Status,
				        lg.GenreName
				    FROM
				        Authors a
				    INNER JOIN
				        LiteraryGenres lg ON a.LiteraryGenreID = lg.LiteraryGenreID
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Author author = new Author();
				author.setAuthorId(rs.getString("AuthorID"));
				author.setName(rs.getString("Name"));
				author.setNationality(rs.getString("Nationality"));
				author.setLiteraryGenreId(rs.getString("LiteraryGenreID"));
				author.setLiteraryGenreName(rs.getString("GenreName"));
				author.setBirthDate(rs.getDate("BirthDate").toLocalDate());
				author.setPhoto(rs.getBytes("Photo"));
				author.setBiography(rs.getString("Biography"));
				author.setStatus(rs.getString("Status"));

				authorList.add(author);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return authorList;
	}

	@Override
	public Author getAuthor(String authorId) {
		Author author = null;

		String sql = """
				    SELECT
				        a.AuthorID, a.Name, a.Nationality,
				        a.LiteraryGenreID, a.BirthDate,
				        a.Photo, a.Biography, a.Status,
				        lg.GenreName
				    FROM
				        Authors a
				    INNER JOIN
				        LiteraryGenres lg ON a.LiteraryGenreID = lg.LiteraryGenreID
				    WHERE
				        a.AuthorID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, authorId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					author = new Author();
					author.setAuthorId(rs.getString("AuthorID"));
					author.setName(rs.getString("Name"));
					author.setNationality(rs.getString("Nationality"));
					author.setLiteraryGenreId(rs.getString("LiteraryGenreID"));
					author.setLiteraryGenreName(rs.getString("GenreName"));
					author.setBirthDate(rs.getDate("BirthDate").toLocalDate());
					author.setPhoto(rs.getBytes("Photo"));
					author.setBiography(rs.getString("Biography"));
					author.setStatus(rs.getString("Status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return author;
	}

	@Override
	public Author createAuthor(Author author) {
		String sqlInsert = """
				    INSERT INTO Authors (Name, Nationality, LiteraryGenreID, BirthDate, Photo, Biography, Status)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		String sqlSelect = """
				    SELECT GenreName
				    FROM LiteraryGenres
				    WHERE LiteraryGenreID = ?
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement psInsert = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {

			psInsert.setString(1, author.getName());
			psInsert.setString(2, author.getNationality());
			psInsert.setString(3, author.getLiteraryGenreId());
			psInsert.setDate(4, java.sql.Date.valueOf(author.getBirthDate()));
			psInsert.setBytes(5, author.getPhoto());
			psInsert.setString(6, author.getBiography());
			psInsert.setString(7, author.getStatus());

			int affectedRows = psInsert.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = psInsert.getGeneratedKeys()) {
					if (rs.next()) {
						author.setAuthorId(rs.getString(1));
					}
				}

				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, author.getLiteraryGenreId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							author.setLiteraryGenreName(rs.getString("GenreName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return author;
	}

	@Override
	public Author updateAuthor(Author author) {
		String sqlUpdate = """
				    UPDATE Authors
				    SET Name = ?, Nationality = ?, LiteraryGenreID = ?,
				        BirthDate = ?, Photo = ?, Biography = ?, Status = ?
				    WHERE AuthorID = ?
				""";

		String sqlSelect = """
				    SELECT GenreName
				    FROM LiteraryGenres
				    WHERE LiteraryGenreID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {

			psUpdate.setString(1, author.getName());
			psUpdate.setString(2, author.getNationality());
			psUpdate.setString(3, author.getLiteraryGenreId());
			psUpdate.setDate(4, java.sql.Date.valueOf(author.getBirthDate()));
			psUpdate.setBytes(5, author.getPhoto());
			psUpdate.setString(6, author.getBiography());
			psUpdate.setString(7, author.getStatus());
			psUpdate.setString(8, author.getAuthorId());

			int resultado = psUpdate.executeUpdate();

			if (resultado > 0) {
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, author.getLiteraryGenreId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							author.setLiteraryGenreName(rs.getString("GenreName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return author;
	}

	@Override
	public List<Author> populateAuthorSelect() {
		List<Author> authorList = new ArrayList<>();

		String sql = """
				    SELECT
				        AuthorID, Name
				    FROM
				        Authors
				    WHERE
				        Status = 'activo'
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Author author = new Author();
				author.setAuthorId(rs.getString("AuthorID"));
				author.setName(rs.getString("Name"));
				authorList.add(author);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return authorList;
	}
}