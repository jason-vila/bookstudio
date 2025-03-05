package com.bookstudio.models;

import java.util.List;
import java.util.Map;

public class DashboardData {
	private int totalActiveBooks;
	private int totalActiveAuthors;
	private int totalActivePublishers;
	private int totalActiveCourses;
	private int totalActiveStudents;
	private int totalActiveLoans;
	private Map<Integer, Integer> loansByMonth;
	private Map<Integer, Integer> returnsByMonth;
	private Map<Integer, Double> averageLoanDurationByMonth;
	private List<MonthlyLoanComparison> monthlyLoanComparison;

	public int getTotalActiveBooks() {
		return totalActiveBooks;
	}

	public void setTotalActiveBooks(int totalActiveBooks) {
		this.totalActiveBooks = totalActiveBooks;
	}

	public int getTotalActiveAuthors() {
		return totalActiveAuthors;
	}

	public void setTotalActiveAuthors(int totalActiveAuthors) {
		this.totalActiveAuthors = totalActiveAuthors;
	}

	public int getTotalActivePublishers() {
		return totalActivePublishers;
	}

	public void setTotalActivePublishers(int totalActivePublishers) {
		this.totalActivePublishers = totalActivePublishers;
	}

	public int getTotalActiveCourses() {
		return totalActiveCourses;
	}

	public void setTotalActiveCourses(int totalActiveCourses) {
		this.totalActiveCourses = totalActiveCourses;
	}

	public int getTotalActiveStudents() {
		return totalActiveStudents;
	}

	public void setTotalActiveStudents(int totalActiveStudents) {
		this.totalActiveStudents = totalActiveStudents;
	}

	public int getTotalActiveLoans() {
		return totalActiveLoans;
	}

	public void setTotalActiveLoans(int totalActiveLoans) {
		this.totalActiveLoans = totalActiveLoans;
	}

	public Map<Integer, Integer> getLoansByMonth() {
		return loansByMonth;
	}

	public void setLoansByMonth(Map<Integer, Integer> loansByMonth) {
		this.loansByMonth = loansByMonth;
	}

	public Map<Integer, Integer> getReturnsByMonth() {
		return returnsByMonth;
	}

	public void setReturnsByMonth(Map<Integer, Integer> returnsByMonth) {
		this.returnsByMonth = returnsByMonth;
	}

	public Map<Integer, Double> getAverageLoanDurationByMonth() {
		return averageLoanDurationByMonth;
	}

	public void setAverageLoanDurationByMonth(Map<Integer, Double> averageLoanDurationByMonth) {
		this.averageLoanDurationByMonth = averageLoanDurationByMonth;
	}

	public List<MonthlyLoanComparison> getMonthlyLoanComparison() {
		return monthlyLoanComparison;
	}

	public void setMonthlyLoanComparison(List<MonthlyLoanComparison> monthlyLoanComparison) {
		this.monthlyLoanComparison = monthlyLoanComparison;
	}
}
