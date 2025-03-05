package com.bookstudio.services;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.dao.DashboardDao;
import com.bookstudio.dao.impl.DashboardDaoImpl;
import com.bookstudio.models.DashboardData;
import com.bookstudio.models.MonthlyLoanComparison;

public class DashboardService {
	private DashboardDao dashboardDao = new DashboardDaoImpl();

	public DashboardData getDashboardData() throws Exception {
		DashboardData data = new DashboardData();

		data.setTotalActiveBooks(dashboardDao.getTotalActiveBooks());
		data.setTotalActiveAuthors(dashboardDao.getTotalActiveAuthors());
		data.setTotalActivePublishers(dashboardDao.getTotalActivePublishers());
		data.setTotalActiveCourses(dashboardDao.getTotalActiveCourses());
		data.setTotalActiveStudents(dashboardDao.getTotalActiveStudents());
		data.setTotalActiveLoans(dashboardDao.getTotalActiveLoans());
		data.setLoansByMonth(dashboardDao.getLoansByMonth());
		data.setReturnsByMonth(dashboardDao.getReturnsByMonth());
		data.setAverageLoanDurationByMonth(dashboardDao.getAverageLoanDurationByMonth());

		int currentYear = LocalDate.now().getYear();
		int previousYear = currentYear - 1;
		List<MonthlyLoanComparison> monthlyComparison = dashboardDao.getMonthlyLoanComparison(previousYear,
				currentYear);
		data.setMonthlyLoanComparison(monthlyComparison);

		return data;
	}
}
