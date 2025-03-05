package com.bookstudio.services;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.dao.BookDao;
import com.bookstudio.dao.LoanDao;
import com.bookstudio.dao.StudentDao;
import com.bookstudio.dao.impl.BookDaoImpl;
import com.bookstudio.dao.impl.LoanDaoImpl;
import com.bookstudio.dao.impl.StudentDaoImpl;
import com.bookstudio.models.Book;
import com.bookstudio.models.Loan;
import com.bookstudio.models.Student;
import com.bookstudio.utils.SelectOptions;

public class LoanService {
	private LoanDao loanDao = new LoanDaoImpl();
	private BookDao bookDao = new BookDaoImpl();
	private StudentDao studentDao = new StudentDaoImpl();

	public List<Loan> listLoans() throws Exception {
		return loanDao.listLoans();
	}

	public Loan getLoan(String loanId) throws Exception {
		return loanDao.getLoan(loanId);
	}

	public Loan createLoan(HttpServletRequest request) throws Exception {
		String bookId = request.getParameter("addLoanBook");
		String studentId = request.getParameter("addLoanStudent");
		LocalDate loanDate = LocalDate.now();
		LocalDate returnDate = LocalDate.parse(request.getParameter("addReturnDate"));
		int quantity = Integer.parseInt(request.getParameter("addLoanQuantity"));
		String status = "prestado";
		String observation = request.getParameter("addLoanObservation");

		Loan loan = new Loan();
		loan.setBookId(bookId);
		loan.setStudentId(studentId);
		loan.setLoanDate(loanDate);
		loan.setReturnDate(returnDate);
		loan.setQuantity(quantity);
		loan.setStatus(status);
		loan.setObservation(observation);

		return loanDao.createLoan(loan);
	}

	public Loan updateLoan(HttpServletRequest request) throws Exception {
		String loanId = request.getParameter("loanId");
		String bookId = request.getParameter("editLoanBook");
		String studentId = request.getParameter("editLoanStudent");
		LocalDate loanDate = LocalDate.parse(request.getParameter("editLoanDate"));
		LocalDate returnDate = LocalDate.parse(request.getParameter("editReturnDate"));
		String observation = request.getParameter("editLoanObservation");

		Loan loan = new Loan();
		loan.setLoanId(loanId);
		loan.setBookId(bookId);
		loan.setStudentId(studentId);
		loan.setLoanDate(loanDate);
		loan.setReturnDate(returnDate);
		loan.setObservation(observation);

		return loanDao.updateLoan(loan);
	}

	public int confirmReturn(String loanId, String newStatus) throws Exception {
		return loanDao.confirmReturn(loanId, newStatus);
	}

	public SelectOptions populateSelects() throws Exception {
		SelectOptions selectOptions = new SelectOptions();

		List<Book> books = bookDao.populateBookSelect();
		selectOptions.setBooks(books);

		List<Student> students = studentDao.populateStudentSelect();
		selectOptions.setStudents(students);

		return selectOptions;
	}
}
