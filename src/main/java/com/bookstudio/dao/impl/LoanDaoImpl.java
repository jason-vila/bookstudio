package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.LoanDao;
import com.bookstudio.models.Loan;
import com.bookstudio.utils.DbConnection;

public class LoanDaoImpl implements LoanDao {
	@Override
	public List<Loan> listLoans() {
		List<Loan> loanList = new ArrayList<>();

		String sql = """
				    SELECT
				        lo.LoanID,
				        lo.BookID,
				        lo.StudentID,
				        lo.LoanDate,
				        lo.ReturnDate,
				        lo.Quantity,
				        lo.Status,
				        lo.Observation,
				        b.Title AS BookTitle,
				        s.FirstName AS StudentFirstName,
				        s.LastName AS StudentLastName,
				        s.DNI AS StudentDNI
				    FROM
				        Loans lo
				    INNER JOIN
				        Books b ON lo.BookID = b.BookID
				    INNER JOIN
				        Students s ON lo.StudentID = s.StudentID
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Loan loan = new Loan();
				loan.setLoanId(rs.getString("LoanID"));
				loan.setBookId(rs.getString("BookID"));
				loan.setStudentId(rs.getString("StudentID"));
				loan.setBookTitle(rs.getString("BookTitle"));

				String fullNameWithDNI = rs.getString("StudentFirstName") + " " + rs.getString("StudentLastName")
						+ " - " + rs.getString("StudentDNI");
				loan.setStudentName(fullNameWithDNI);

				loan.setLoanDate(rs.getDate("LoanDate").toLocalDate());
				loan.setReturnDate(rs.getDate("ReturnDate").toLocalDate());
				loan.setQuantity(rs.getInt("Quantity"));
				loan.setStatus(rs.getString("Status"));
				loan.setObservation(rs.getString("Observation"));

				loanList.add(loan);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return loanList;
	}

	@Override
	public Loan getLoan(String loanId) {
		Loan loan = null;

		String sql = """
				    SELECT
				        lo.LoanID,
				        lo.BookID,
				        lo.StudentID,
				        lo.LoanDate,
				        lo.ReturnDate,
				        lo.Quantity,
				        lo.Status,
				        lo.Observation,
				        b.Title AS BookTitle,
				        s.FirstName AS StudentFirstName,
				        s.LastName AS StudentLastName,
				        s.DNI AS StudentDNI
				    FROM
				        Loans lo
				    INNER JOIN
				        Books b ON lo.BookID = b.BookID
				    INNER JOIN
				        Students s ON lo.StudentID = s.StudentID
				    WHERE
				        lo.LoanID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, loanId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					loan = new Loan();
					loan.setLoanId(rs.getString("LoanID"));
					loan.setBookId(rs.getString("BookID"));
					loan.setStudentId(rs.getString("StudentID"));
					loan.setBookTitle(rs.getString("BookTitle"));

					String fullNameWithDNI = rs.getString("StudentFirstName") + " " + rs.getString("StudentLastName")
							+ " - " + rs.getString("StudentDNI");
					loan.setStudentName(fullNameWithDNI);

					loan.setLoanDate(rs.getDate("LoanDate").toLocalDate());
					loan.setReturnDate(rs.getDate("ReturnDate").toLocalDate());
					loan.setQuantity(rs.getInt("Quantity"));
					loan.setStatus(rs.getString("Status"));
					loan.setObservation(rs.getString("Observation"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return loan;
	}

	@Override
	public Loan createLoan(Loan loan) {
		String sqlInsert = """
				    INSERT INTO Loans (BookID, StudentID, LoanDate, ReturnDate, Quantity, Status, Observation)
				    VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		String sqlUpdate = """
				    UPDATE Books
				    SET LoanedCopies = LoanedCopies + ?
				    WHERE BookID = ?
				""";

		String sqlSelect = """
				    SELECT
				        b.Title AS BookTitle,
				        s.FirstName AS StudentFirstName,
				        s.LastName AS StudentLastName,
				        s.DNI AS StudentDNI
				    FROM
				        Loans lo
				    INNER JOIN
				        Books b ON lo.BookID = b.BookID
				    INNER JOIN
				        Students s ON lo.StudentID = s.StudentID
				    WHERE
				        lo.LoanID = ?
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement psInsert = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {

			psInsert.setString(1, loan.getBookId());
			psInsert.setString(2, loan.getStudentId());
			psInsert.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
			psInsert.setDate(4, java.sql.Date.valueOf(loan.getReturnDate()));
			psInsert.setInt(5, loan.getQuantity());
			psInsert.setString(6, loan.getStatus());
			psInsert.setString(7, loan.getObservation());

			int affectedRows = psInsert.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
					if (rsKeys.next()) {
						loan.setLoanId(rsKeys.getString(1));
					}
				}
				
				try (PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {
					psUpdate.setInt(1, loan.getQuantity());
					psUpdate.setString(2, loan.getBookId());
					psUpdate.executeUpdate();
				}
				
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, loan.getLoanId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							loan.setBookTitle(rs.getString("BookTitle"));
							String fullNameWithDNI = rs.getString("StudentFirstName") + " "
									+ rs.getString("StudentLastName") + " - " + rs.getString("StudentDNI");
							loan.setStudentName(fullNameWithDNI);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return loan;
	}

	@Override
	public Loan updateLoan(Loan loan) {
		String sqlUpdate = """
				    UPDATE Loans
				    SET StudentID = ?, LoanDate = ?, ReturnDate = ?, Observation = ?
				    WHERE LoanID = ?
				""";

		String sqlSelect = """
				    SELECT
				        b.Title AS BookTitle,
				        s.FirstName AS StudentFirstName,
				        s.LastName AS StudentLastName,
				        s.DNI AS StudentDNI,
				        lo.Quantity
				    FROM
				        Loans lo
				    INNER JOIN
				        Books b ON lo.BookID = b.BookID
				    INNER JOIN
				        Students s ON lo.StudentID = s.StudentID
				    WHERE
				        lo.LoanID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {

			psUpdate.setString(1, loan.getStudentId());
			psUpdate.setDate(2, java.sql.Date.valueOf(loan.getLoanDate()));
			psUpdate.setDate(3, java.sql.Date.valueOf(loan.getReturnDate()));
			psUpdate.setString(4, loan.getObservation());
			psUpdate.setString(5, loan.getLoanId());

			int resultado = psUpdate.executeUpdate();

			if (resultado > 0) {
				try (PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {
					psSelect.setString(1, loan.getLoanId());
					try (ResultSet rs = psSelect.executeQuery()) {
						if (rs.next()) {
							loan.setBookTitle(rs.getString("BookTitle"));
							String fullNameWithDNI = rs.getString("StudentFirstName") + " "
									+ rs.getString("StudentLastName") + " - " + rs.getString("StudentDNI");
							loan.setStudentName(fullNameWithDNI);
							loan.setQuantity(rs.getInt("Quantity"));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return loan;
	}

	@Override
	public int confirmReturn(String loanId, String newStatus) {
		String sqlSelect = "SELECT Quantity, BookID, Status FROM Loans WHERE LoanID = ?";

		String sqlUpdateState = "UPDATE Loans SET Status = ? WHERE LoanID = ?";

		String sqlUpdateBook = "UPDATE Books SET LoanedCopies = LoanedCopies - ? WHERE BookID = ?";

		int quantity = 0;
		try (Connection cn = DbConnection.getConexion(); PreparedStatement psSelect = cn.prepareStatement(sqlSelect)) {

			psSelect.setString(1, loanId);
			try (ResultSet rs = psSelect.executeQuery()) {
				if (rs.next()) {
					quantity = rs.getInt("Quantity");
					int bookId = rs.getInt("BookID");
					String currentState = rs.getString("Status");

					if ("devuelto".equalsIgnoreCase(currentState)) {
						return 0;
					}

					try (PreparedStatement psUpdateState = cn.prepareStatement(sqlUpdateState)) {
						psUpdateState.setString(1, newStatus);
						psUpdateState.setString(2, loanId);
						psUpdateState.executeUpdate();
					}

					if ("devuelto".equalsIgnoreCase(newStatus)) {
						try (PreparedStatement psUpdateBook = cn.prepareStatement(sqlUpdateBook)) {
							psUpdateBook.setInt(1, quantity);
							psUpdateBook.setInt(2, bookId);
							psUpdateBook.executeUpdate();
						}
					}
					return 1;
				} else {
					System.out.println("No se encontró el préstamo con el ID: " + loanId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
