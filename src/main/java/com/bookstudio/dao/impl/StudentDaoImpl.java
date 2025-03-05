package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.StudentDao;
import com.bookstudio.models.Student;
import com.bookstudio.utils.DbConnection;

public class StudentDaoImpl implements StudentDao {
	@Override
	public List<Student> listStudents() {
		List<Student> studentList = new ArrayList<>();

		String sql = "SELECT s.StudentID, s.DNI, s.FirstName, s.LastName, s.Address, " +
					"s.Phone, s.Email, s.BirthDate, s.Gender, " +
					"f.FacultyID, f.FacultyName, s.Status " +
					"FROM Students s " +
					"JOIN Faculties f ON s.FacultyID = f.FacultyID";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Student student = new Student();
				student.setStudentId(rs.getString("StudentID"));
				student.setDni(rs.getString("DNI"));
				student.setFirstName(rs.getString("FirstName"));
				student.setLastName(rs.getString("LastName"));
				student.setAddress(rs.getString("Address"));
				student.setPhone(rs.getString("Phone"));
				student.setEmail(rs.getString("Email"));
				student.setBirthDate(rs.getDate("BirthDate").toLocalDate());
				student.setGender(rs.getString("Gender"));
				student.setFacultyId(rs.getString("FacultyID"));
				student.setFacultyName(rs.getString("FacultyName"));
				student.setStatus(rs.getString("Status"));

				studentList.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return studentList;
	}

	@Override
	public Student getStudent(String studentId) {
		Student student = null;

		String sql = """
				    SELECT s.StudentID, s.DNI, s.FirstName, s.LastName, s.Address,
				           s.Phone, s.Email, s.BirthDate, s.Gender,
				           s.FacultyID, f.FacultyName, s.Status
				    FROM Students s
				    JOIN Faculties f ON s.FacultyID = f.FacultyID
				    WHERE s.StudentID = ?
				""";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, studentId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					student = new Student();
					student.setStudentId(rs.getString("StudentID"));
					student.setDni(rs.getString("DNI"));
					student.setFirstName(rs.getString("FirstName"));
					student.setLastName(rs.getString("LastName"));
					student.setAddress(rs.getString("Address"));
					student.setPhone(rs.getString("Phone"));
					student.setEmail(rs.getString("Email"));
					student.setBirthDate(rs.getDate("BirthDate").toLocalDate());
					student.setGender(rs.getString("Gender"));
					student.setFacultyId(rs.getString("FacultyID"));
					student.setFacultyName(rs.getString("FacultyName"));
					student.setStatus(rs.getString("Status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return student;
	}

	@Override
	public Student createStudent(Student student) throws SQLException {
		String sqlCheckDni = "SELECT COUNT(*) FROM Students WHERE DNI = ?";

		String sqlCheckEmail = "SELECT COUNT(*) FROM Students WHERE Email = ?";

		String sqlInsert = "INSERT INTO Students (DNI, FirstName, LastName, Address, Phone, Email, BirthDate, Gender, FacultyID, Status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		String sqlSelectFaculty = "SELECT FacultyName FROM Faculties WHERE FacultyID = ?";

		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = DbConnection.getConexion();

			ps = cn.prepareStatement(sqlCheckDni);
			ps.setString(1, student.getDni());
			rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				throw new SQLException("El DNI ingresado ya ha sido registrado.");
			}
			rs.close();
			ps.close();

			ps = cn.prepareStatement(sqlCheckEmail);
			ps.setString(1, student.getEmail());
			rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				throw new SQLException("El correo electrónico ingresado ya ha sido registrado.");
			}
			rs.close();
			ps.close();

			ps = cn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, student.getDni());
			ps.setString(2, student.getFirstName());
			ps.setString(3, student.getLastName());
			ps.setString(4, student.getAddress());
			ps.setString(5, student.getPhone());
			ps.setString(6, student.getEmail());
			ps.setDate(7, java.sql.Date.valueOf(student.getBirthDate()));
			ps.setString(8, student.getGender());
			ps.setString(9, student.getFacultyId());
			ps.setString(10, student.getStatus());

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						student.setStudentId(generatedKeys.getString(1));
					}
				}
			}
			ps.close();

			ps = cn.prepareStatement(sqlSelectFaculty);
			ps.setString(1, student.getFacultyId());
			rs = ps.executeQuery();
			if (rs.next()) {
				student.setFacultyName(rs.getString("FacultyName"));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (cn != null) try { cn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		return student;
	}

	@Override
	public Student updateStudent(Student student) throws SQLException {
		String sqlCheckEmail = "SELECT COUNT(*) FROM Students WHERE Email = ? AND StudentID != ?";

		String sqlUpdate = "UPDATE Students SET Address = ?, Phone = ?, Email = ?, Gender = ?, FacultyID = ?, Status = ? "
				+ "WHERE StudentID = ?";

		String sqlSelectFaculty = "SELECT FacultyName FROM Faculties WHERE FacultyID = ?";

		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = DbConnection.getConexion();

			ps = cn.prepareStatement(sqlCheckEmail);
			ps.setString(1, student.getEmail());
			ps.setString(2, student.getStudentId());
			rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				throw new SQLException("El correo electrónico ingresado ya ha sido registrado.");
			}
			rs.close();
			ps.close();

			ps = cn.prepareStatement(sqlUpdate);
			ps.setString(1, student.getAddress());
			ps.setString(2, student.getPhone());
			ps.setString(3, student.getEmail());
			ps.setString(4, student.getGender());
			ps.setString(5, student.getFacultyId());
			ps.setString(6, student.getStatus());
			ps.setString(7, student.getStudentId());

			int result = ps.executeUpdate();
			ps.close();

			if (result > 0) {
				ps = cn.prepareStatement(sqlSelectFaculty);
				ps.setString(1, student.getFacultyId());
				rs = ps.executeQuery();
				if (rs.next()) {
					student.setFacultyName(rs.getString("FacultyName"));
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (cn != null) try { cn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		return student;
	}

	@Override
	public List<Student> populateStudentSelect() {
		List<Student> studentList = new ArrayList<>();

		String sql = """
				    SELECT StudentID, FirstName, LastName, DNI
				    FROM Students
				    WHERE Status = 'activo'
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Student student = new Student();
				student.setStudentId(rs.getString("StudentID"));
				String fullNameWithDNI = rs.getString("FirstName") + " " + rs.getString("LastName") + " - "
						+ rs.getString("DNI");
				student.setFirstName(fullNameWithDNI);
				studentList.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return studentList;
	}
}
