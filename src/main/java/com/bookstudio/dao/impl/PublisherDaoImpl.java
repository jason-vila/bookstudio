package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.PublisherDao;
import com.bookstudio.models.Publisher;
import com.bookstudio.utils.DbConnection;

public class PublisherDaoImpl implements PublisherDao {
	@Override
	public List<Publisher> listPublishers() {
		List<Publisher> publisherList = new ArrayList<>();

		String sql = """
				    SELECT
				        p.PublisherID, p.Name, p.Nationality,
				        p.LiteraryGenreID, p.Photo, p.FoundationYear,
				        p.Website, p.Address, p.Status,
				        lg.GenreName AS LiteraryGenre
				    FROM
				        Publishers p
				    INNER JOIN
				        LiteraryGenres lg ON p.LiteraryGenreID = lg.LiteraryGenreID
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Publisher publisher = new Publisher();
				publisher.setPublisherId(rs.getString("PublisherID"));
				publisher.setName(rs.getString("Name"));
				publisher.setNationality(rs.getString("Nationality"));
				publisher.setLiteraryGenreId(rs.getString("LiteraryGenreID"));
				publisher.setLiteraryGenreName(rs.getString("LiteraryGenre"));
				publisher.setPhoto(rs.getBytes("Photo"));
				publisher.setFoundationYear(rs.getInt("FoundationYear"));
				publisher.setWebsite(rs.getString("Website"));
				publisher.setAddress(rs.getString("Address"));
				publisher.setStatus(rs.getString("Status"));

				publisherList.add(publisher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return publisherList;
	}

	@Override
	public Publisher getPublisher(String publisherId) {
		Publisher publisher = null;

		String sql = """
				    SELECT
				        p.PublisherID, p.Name, p.Nationality,
				        p.LiteraryGenreID, p.Photo, p.FoundationYear,
				        p.Website, p.Address, p.Status,
				        lg.GenreName AS LiteraryGenre
				    FROM
				        Publishers p
				    INNER JOIN
				        LiteraryGenres lg ON p.LiteraryGenreID = lg.LiteraryGenreID
				    WHERE
				        p.PublisherID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, publisherId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					publisher = new Publisher();
					publisher.setPublisherId(rs.getString("PublisherID"));
					publisher.setName(rs.getString("Name"));
					publisher.setNationality(rs.getString("Nationality"));
					publisher.setLiteraryGenreId(rs.getString("LiteraryGenreID"));
					publisher.setLiteraryGenreName(rs.getString("LiteraryGenre"));
					publisher.setPhoto(rs.getBytes("Photo"));
					publisher.setFoundationYear(rs.getInt("FoundationYear"));
					publisher.setWebsite(rs.getString("Website"));
					publisher.setAddress(rs.getString("Address"));
					publisher.setStatus(rs.getString("Status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return publisher;
	}

	@Override
	public Publisher createPublisher(Publisher publisher) {
		String sqlInsert = """
				    INSERT INTO Publishers (
				        Name, Nationality, LiteraryGenreID, Photo,
				        FoundationYear, Website, Address, Status
				    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		String sqlSelectGenre = """
				    SELECT GenreName
				    FROM LiteraryGenres
				    WHERE LiteraryGenreID = ?
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement psInsert = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {

			psInsert.setString(1, publisher.getName());
			psInsert.setString(2, publisher.getNationality());
			psInsert.setString(3, publisher.getLiteraryGenreId());
			psInsert.setBytes(4, publisher.getPhoto());
			psInsert.setInt(5, publisher.getFoundationYear());
			psInsert.setString(6, publisher.getWebsite());
			psInsert.setString(7, publisher.getAddress());
			psInsert.setString(8, publisher.getStatus());

			int affectedRows = psInsert.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = psInsert.getGeneratedKeys()) {
					if (rs.next()) {
						publisher.setPublisherId(rs.getString(1));
					}
				}

				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelectGenre)) {
					psSelect.setString(1, publisher.getLiteraryGenreId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							publisher.setLiteraryGenreName(rs.getString("GenreName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return publisher;
	}

	@Override
	public Publisher updatePublisher(Publisher publisher) {
		String sqlUpdate = """
				    UPDATE Publishers
				    SET Name = ?, Nationality = ?, LiteraryGenreID = ?,
				        Photo = ?, FoundationYear = ?, Website = ?,
				        Address = ?, Status = ?
				    WHERE PublisherID = ?
				""";

		String sqlSelectGenre = """
				    SELECT GenreName
				    FROM LiteraryGenres
				    WHERE LiteraryGenreID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {

			psUpdate.setString(1, publisher.getName());
			psUpdate.setString(2, publisher.getNationality());
			psUpdate.setString(3, publisher.getLiteraryGenreId());
			psUpdate.setBytes(4, publisher.getPhoto());
			psUpdate.setInt(5, publisher.getFoundationYear());
			psUpdate.setString(6, publisher.getWebsite());
			psUpdate.setString(7, publisher.getAddress());
			psUpdate.setString(8, publisher.getStatus());
			psUpdate.setString(9, publisher.getPublisherId());

			int resultado = psUpdate.executeUpdate();

			if (resultado > 0) {
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelectGenre)) {
					psSelect.setString(1, publisher.getLiteraryGenreId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							publisher.setLiteraryGenreName(rs.getString("GenreName"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return publisher;
	}

	@Override
	public List<Publisher> populatePublisherSelect() {
		List<Publisher> publisherList = new ArrayList<>();

		String sql = """
				    SELECT
				        PublisherID, Name
				    FROM
				        Publishers
				    WHERE
				        Status = 'activo'
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Publisher publisher = new Publisher();
				publisher.setPublisherId(rs.getString("PublisherID"));
				publisher.setName(rs.getString("Name"));
				publisherList.add(publisher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return publisherList;
	}
}
