package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bookstudio.dao.DashboardDao;
import com.bookstudio.models.MonthlyLoanComparison;
import com.bookstudio.utils.DbConnection;

public class DashboardDaoImpl implements DashboardDao {
	@Override
	public int getTotalActiveBooks() {
		String sql = "SELECT COUNT(*) FROM Books WHERE Status = 'activo'";
		return getCount(sql);
	}

	@Override
	public int getTotalActiveAuthors() {
		String sql = "SELECT COUNT(*) FROM Authors WHERE Status = 'activo'";
		return getCount(sql);
	}

	@Override
	public int getTotalActivePublishers() {
		String sql = "SELECT COUNT(*) FROM Publishers WHERE Status = 'activo'";
		return getCount(sql);
	}

	@Override
	public int getTotalActiveCourses() {
		String sql = "SELECT COUNT(*) FROM Courses WHERE Status = 'activo'";
		return getCount(sql);
	}

	@Override
	public int getTotalActiveStudents() {
		String sql = "SELECT COUNT(*) FROM Students WHERE Status = 'activo'";
		return getCount(sql);
	}

	@Override
	public int getTotalActiveLoans() {
		String sql = "SELECT COUNT(*) FROM Loans WHERE Status = 'prestado'";
		return getCount(sql);
	}

	@Override
	public Map<Integer, Integer> getLoansByMonth() {
		String sql = """
				    SELECT MONTH(LoanDate) AS Month, COUNT(*) AS Total
				    FROM Loans
				    WHERE YEAR(LoanDate) = YEAR(CURDATE())
				    GROUP BY MONTH(LoanDate)
				""";
		
		return getMonthlyData(sql);
	}

	@Override
	public Map<Integer, Integer> getReturnsByMonth() {
		String sql = """
					SELECT MONTH(ReturnDate) AS Month, COUNT(*) AS Total
					FROM Loans
					WHERE YEAR(ReturnDate) = YEAR(CURDATE())
					AND ReturnDate IS NOT NULL
					AND status = 'devuelto'
					GROUP BY MONTH(ReturnDate)
				""";
		
		return getMonthlyData(sql);
	}

	@Override
	public Map<Integer, Double> getAverageLoanDurationByMonth() {
		String sql = """
				    SELECT MONTH(LoanDate) AS Month, AVG(DATEDIFF(ReturnDate, LoanDate)) AS AvgDuration
				    FROM Loans
				    WHERE YEAR(LoanDate) = YEAR(CURDATE()) AND ReturnDate IS NOT NULL
				    GROUP BY MONTH(LoanDate)
				""";

		Map<Integer, Double> data = new HashMap<>();
		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				data.put(rs.getInt("Month"), rs.getDouble("AvgDuration"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	@Override
	public List<MonthlyLoanComparison> getMonthlyLoanComparison(int year1, int year2) {
		String sql = """
				    SELECT
				        MONTH(LoanDate) AS Month,
				        SUM(CASE WHEN YEAR(LoanDate) = ? THEN 1 ELSE 0 END) AS LoansYear1,
				        SUM(CASE WHEN YEAR(LoanDate) = ? THEN 1 ELSE 0 END) AS LoansYear2
				    FROM Loans
				    WHERE YEAR(LoanDate) IN (?, ?)
				    GROUP BY MONTH(LoanDate)
				    ORDER BY Month
				""";

		List<MonthlyLoanComparison> comparisonList = new ArrayList<>();
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setInt(1, year1);
			ps.setInt(2, year2);
			ps.setInt(3, year1);
			ps.setInt(4, year2);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					comparisonList.add(new MonthlyLoanComparison(rs.getInt("Month"), rs.getInt("LoansYear1"),
							rs.getInt("LoansYear2")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return comparisonList;
	}

	private int getCount(String sql) {
		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	private Map<Integer, Integer> getMonthlyData(String sql) {
		Map<Integer, Integer> data = new HashMap<>();
		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				data.put(rs.getInt("Month"), rs.getInt("Total"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
