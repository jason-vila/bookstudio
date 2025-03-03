package com.bookstudio.dao;

import java.sql.SQLException;
import java.util.List;

import com.bookstudio.models.Student;

public interface StudentDao {
	List<Student> listStudents();
	Student getStudent(String studentId);
	Student createStudent(Student student) throws SQLException;
	Student updateStudent(Student student) throws SQLException;
	List<Student> populateStudentSelect();
}
