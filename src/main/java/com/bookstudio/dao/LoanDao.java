package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Loan;

public interface LoanDao {
	public List<Loan> listLoans();
	public Loan getLoan(String loanId);
	public Loan createLoan(Loan loan);
	public Loan updateLoan(Loan loan);
	public int confirmReturn(String loanId, String newStatus);
}
