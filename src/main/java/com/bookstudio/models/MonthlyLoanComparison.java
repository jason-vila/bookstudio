package com.bookstudio.models;

public class MonthlyLoanComparison {
	private int month;
	private int loansYear1;
	private int loansYear2;

	public MonthlyLoanComparison(int month, int loansYear1, int loansYear2) {
		this.month = month;
		this.loansYear1 = loansYear1;
		this.loansYear2 = loansYear2;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getLoansYear1() {
		return loansYear1;
	}

	public void setLoansYear1(int loansYear1) {
		this.loansYear1 = loansYear1;
	}

	public int getLoansYear2() {
		return loansYear2;
	}

	public void setLoansYear2(int loansYear2) {
		this.loansYear2 = loansYear2;
	}

	@Override
	public String toString() {
		return "MonthlyLoanComparison{" +
				"month=" + month +
				", loansYear1=" + loansYear1 +
				", loansYear2=" + loansYear2 +
				'}';
	}
}
