package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Course;

public interface CourseDao {
	public List<Course> listCourses();
	public Course getCourse(String courseId);
	public Course createCourse(Course course);
	public Course updateCourse(Course course);
	public List<Course> populateCourseSelect();
}
