$(document).ready(function() {
	// Initialize charts with Chart.js using jQuery

	// Borrowed Books Chart
	var borrowedBooksChart = new Chart($('#borrowedBooksChart'), {
		type: 'bar',
		data: {
			labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
			datasets: [{
				label: 'Libros Prestados',
				data: [],
				backgroundColor: 'rgba(54, 162, 235, 0.2)',
				borderColor: 'rgba(54, 162, 235, 1)',
				borderWidth: 1
			}]
		},
		options: {
			scales: { y: { beginAtZero: true } }
		}
	});

	// Returned Books Chart
	var returnedBooksChart = new Chart($('#returnedBooksChart'), {
		type: 'bar',
		data: {
			labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
			datasets: [{
				label: 'Libros Devueltos',
				data: [],
				backgroundColor: 'rgba(75, 192, 192, 0.2)',
				borderColor: 'rgba(75, 192, 192, 1)',
				borderWidth: 1
			}]
		},
		options: {
			scales: { y: { beginAtZero: true } }
		}
	});

	// Average Loan Time Chart
	var avgLoanTimeChart = new Chart($('#avgLoanTimeChart'), {
		type: 'line',
		data: {
			labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
			datasets: [{
				label: 'Duración Promedio de Préstamo (días)',
				data: [],
				borderColor: 'rgba(255, 99, 132, 1)',
				backgroundColor: 'rgba(255, 99, 132, 0.2)',
				borderWidth: 2,
				tension: 0.4
			}]
		},
		options: {
			scales: { y: { beginAtZero: true } }
		}
	});

	// Loan Comparison Chart
	var loanComparisonChart = new Chart($('#loanComparisonChart'), {
		type: 'bar',
		data: {
			labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
			datasets: [
				{ label: 'Año Anterior', data: [], backgroundColor: 'rgba(54, 162, 235, 0.7)' },
				{ label: 'Año Actual', data: [], backgroundColor: 'rgba(255, 99, 132, 0.7)' }
			]
		},
		options: {
			responsive: true,
			scales: { y: { beginAtZero: true } }
		}
	});

	// AJAX call to get real dashboard data
	$.ajax({
		url: '/bookstudio/DashboardServlet',
		type: 'GET',
		data: { type: 'getDashboardData' },
		dataType: 'json',
		success: function(data) {
			var loansData = [];
			var returnsData = [];
			var avgDurationData = [];

			for (var m = 1; m <= 6; m++) {
				loansData.push(data.loansByMonth[m] || 0);
				returnsData.push(data.returnsByMonth[m] || 0);
				avgDurationData.push(data.averageLoanDurationByMonth[m] || 0);
			}

			// Update charts with server data
			borrowedBooksChart.data.datasets[0].data = loansData;
			borrowedBooksChart.update();

			returnedBooksChart.data.datasets[0].data = returnsData;
			returnedBooksChart.update();

			avgLoanTimeChart.data.datasets[0].data = avgDurationData;
			avgLoanTimeChart.update();

			// Process loan comparison data
			var comparison = data.monthlyLoanComparison;
			comparison.sort(function(a, b) { return a.month - b.month; });

			var year1Data = [];
			var year2Data = [];

			for (var i = 1; i <= 6; i++) {
				var comp = comparison.find(function(item) { return item.month === i; });
				if (comp) {
					year1Data.push(comp.loansYear1);
					year2Data.push(comp.loansYear2);
				} else {
					year1Data.push(0);
					year2Data.push(0);
				}
			}

			loanComparisonChart.data.datasets[0].data = year1Data;
			loanComparisonChart.data.datasets[1].data = year2Data;
			loanComparisonChart.update();

			$('#totalBooks').text(data.totalActiveBooks + " Libros Activos");
			$('#totalAuthors').text(data.totalActiveAuthors + " Autores Activos");
			$('#totalPublishers').text(data.totalActivePublishers + " Editoriales Activas");
			$('#totalCourses').text(data.totalActiveCourses + " Cursos Activos");
			$('#totalStudents').text(data.totalActiveStudents + " Estudiantes Activos");
			$('#totalLoans').text(data.totalActiveLoans + " Préstamos Activos");
		},
		error: function(err) {
			console.error('Error al recuperar datos del dashboard:', err);
		}
	});
});