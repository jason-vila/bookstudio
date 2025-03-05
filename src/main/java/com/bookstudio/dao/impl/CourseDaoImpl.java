package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.dao.CourseDao;
import com.bookstudio.models.Course;
import com.bookstudio.utils.DbConnection;

public class CourseDaoImpl implements CourseDao {
	@Override
	public List<Course> listCourses() {
		List<Course> courseList = new ArrayList<>();

		String sql = "SELECT CourseID, Name, Level, Description, Status FROM Courses";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Course course = new Course();
				course.setCourseId(rs.getString("CourseID"));
				course.setName(rs.getString("Name"));
				course.setLevel(rs.getString("Level"));
				course.setDescription(rs.getString("Description"));
				course.setStatus(rs.getString("Status"));
				courseList.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courseList;
	}

	@Override
	public Course getCourse(String courseId) {
		Course course = null;

		String sql = "SELECT CourseID, Name, Level, Description, Status FROM Courses WHERE CourseID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, courseId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					course = new Course();
					course.setCourseId(rs.getString("CourseID"));
					course.setName(rs.getString("Name"));
					course.setLevel(rs.getString("Level"));
					course.setDescription(rs.getString("Description"));
					course.setStatus(rs.getString("Status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return course;
	}

	@Override
	public Course createCourse(Course course) {
		String sql = "INSERT INTO Courses (Name, Level, Description, Status) VALUES (?, ?, ?, ?)";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, course.getName());
			ps.setString(2, course.getLevel());
			ps.setString(3, course.getDescription());
			ps.setString(4, course.getStatus());

			int rowsInserted = ps.executeUpdate();

			if (rowsInserted > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						course.setCourseId(rs.getString(1));
					}
				}
			} else {
				System.out.println("No course was inserted.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return course;
	}

	@Override
	public Course updateCourse(Course course) {
		String sql = "UPDATE Courses SET Name = ?, Level = ?, Description = ?, Status = ? WHERE CourseID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, course.getName());
			ps.setString(2, course.getLevel());
			ps.setString(3, course.getDescription());
			ps.setString(4, course.getStatus());
			ps.setString(5, course.getCourseId());

			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated < 0) {
				System.out.println("No course found to update.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return course;
	}

	@Override
	public List<Course> populateCourseSelect() {
		List<Course> courseList = new ArrayList<>();

		String sql = """
				    SELECT
				        CourseID, Name
				    FROM
				        Courses
				    WHERE
				        Status = 'activo'
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Course course = new Course();
				course.setCourseId(rs.getString("CourseID"));
				course.setName(rs.getString("Name"));
				courseList.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courseList;
	}
}
