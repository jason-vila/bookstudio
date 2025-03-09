/**
 * students.js
 * Manages the initialization, data loading, and configuration of the students table,  
 * as well as handling modals for creating, viewing, editing student details, 
 * and performing logical delete (status change) operations on students.
 * Utilizes AJAX for CRUD operations on student data.
 * Includes functions to manage UI elements like placeholders, dropdown styles, and tooltips.
 * Additionally, incorporates functionality to generate PDFs and Excel files directly from the datatable.
 * 
 * @author [Jason]
 */

/*****************************************
 * GLOBAL VARIABLES AND HELPER FUNCTIONS
 *****************************************/

// Global list of faculties for the selectpickers
var facultyList = [];

function populateSelect(selector, dataList, valueKey, textKey) {
	var select = $(selector).selectpicker('destroy').empty();
	dataList.forEach(item => {
		if (item[valueKey]) {
			select.append(
				$('<option>', {
					value: item[valueKey],
					text: item[textKey]
				})
			);
		}
	});
}

function populateSelectOptions() {
	$.ajax({
		url: '/bookstudio/StudentServlet',
		type: 'GET',
		data: { type: 'populateSelects' },
		dataType: 'json',
		success: function(data) {
			if (data) {
				facultyList = data.faculties;

				populateSelect('#addStudentFaculty', facultyList, 'facultyId', 'facultyName');

				populateSelect('#editStudentFaculty', facultyList, 'facultyId', 'facultyName');
			}
		},
		error: function(status, error) {
			console.error("Error al obtener los datos para los select:", status, error);
		}
	});
}

function placeholderColorSelect() {
	$('select.selectpicker').on('change', function() {
		var $select = $(this);
		var $dropdown = $select.closest('.bootstrap-select');
		var $filterOption = $dropdown.find('.filter-option-inner-inner');

		if ($select.val() !== "" && $select.val() !== null) {
			$dropdown.removeClass('placeholder-color');
			$filterOption.css('color', 'var(--bs-body-color)');
		}
	});
}

function placeholderColorEditSelect() {
	$('select[id^="edit"]').each(function() {
		var $select = $(this);
		var $dropdown = $select.closest('.bootstrap-select');
		var $filterOption = $dropdown.find('.filter-option-inner-inner');

		if ($filterOption.text().trim() === "No hay selección") {
			$filterOption.css('color', 'var(--placeholder-color)');
		} else {
			$filterOption.css('color', 'var(--bs-body-color)');
		}
	});
}

function placeholderColorDateInput() {
	$('input[type="date"]').each(function() {
		var $input = $(this);

		if (!$input.val()) {
			$input.css('color', 'var(--placeholder-color)');
		} else {
			$input.css('color', '');
		}
	});

	$('input[type="date"]').on('change input', function() {
		var $input = $(this);

		if (!$input.val()) {
			$input.css('color', 'var(--placeholder-color)');
		} else {
			$input.css('color', '');
		}
	});
}

/*****************************************
 * TABLE HANDLING
 *****************************************/

function generateRow(student) {
	const userRole = sessionStorage.getItem('userRole');

	return `
		<tr>
			<td class="align-middle text-start">${student.studentId}</td>
			<td class="align-middle text-start">${student.dni}</td>
			<td class="align-middle text-start">${student.firstName}</td>
			<td class="align-middle text-start">${student.lastName}</td>
			<td class="align-middle text-start">${student.phone}</td>
			<td class="align-middle text-start">${student.email}</td>
			<td class="align-middle text-center">
				${student.status === 'activo'
					? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle p-1">Activo</span>'
					: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle p-1">Inactivo</span>'}
			</td>
			<td class="align-middle text-center">
				<div class="d-inline-flex gap-2">
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Detalles"
						data-bs-toggle="modal" data-bs-target="#detailsStudentModal" data-id="${student.studentId}">
						<i class="bi bi-eye"></i>
					</button>
					${userRole === 'administrador' ?
						`<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Editar"
							data-bs-toggle="modal" data-bs-target="#editStudentModal" data-id="${student.studentId}">
							<i class="bi bi-pencil"></i>
						</button>`
					: ''}
				</div>
			</td>
		</tr>
	`;
}

function addRowToTable(student) {
	var table = $('#studentTable').DataTable();
	var rowHtml = generateRow(student);
	var $row = $(rowHtml);

	table.row.add($row).draw();

	initializeTooltips($row);
}

function loadStudents() {
	toggleButtonAndSpinner('loading');

	let safetyTimer = setTimeout(function() {
		toggleButtonAndSpinner('loaded');
		$('#tableContainer').removeClass('d-none');
		$('#cardContainer').removeClass('h-100');
	}, 8000);

	$.ajax({
		url: '/bookstudio/StudentServlet',
		type: 'GET',
		data: { type: 'list' },
		dataType: 'json',
		success: function(data) {
			clearTimeout(safetyTimer);

			var tableBody = $('#bodyStudents');
			tableBody.empty();

			if (data && data.length > 0) {
				data.forEach(function(student) {
					var row = generateRow(student);
					tableBody.append(row);
				});

				initializeTooltips(tableBody);
			}

			if ($.fn.DataTable.isDataTable('#bookTable')) {
				$('#studentTable').DataTable().destroy();
			}

			let dataTable = setupDataTable('#studentTable');

			if (data && data.length > 0) {
				$("#generatePDF, #generateExcel").prop("disabled", false);
			} else {
				$("#generatePDF, #generateExcel").prop("disabled", true);
			}

			dataTable.on('draw', function() {
				const filteredCount = dataTable.rows({ search: 'applied' }).count();
				const noDataMessage = $("#authorTable").find("td.dataTables_empty").length > 0;
				$("#generatePDF, #generateExcel").prop("disabled", filteredCount === 0 || noDataMessage);
			});

			$("#generatePDF").off("click").on("click", function() {
				generatePDF(dataTable);
			});

			$("#generateExcel").off("click").on("click", function() {
				generateExcel(dataTable);
			});
		},
		error: function(status, error) {
			clearTimeout(safetyTimer);
			console.error("Error en la solicitud AJAX:", status, error);

			var tableBody = $('#bodyStudents');
			tableBody.empty();

			if ($.fn.DataTable.isDataTable('#studentTable')) {
				$('#studentTable').DataTable().destroy();
			}

			setupDataTable('#studentTable');
		}
	});
}

function updateRowInTable(student) {
	var table = $('#studentTable').DataTable();

	var row = table.rows().nodes().to$().filter(function() {
		return $(this).find('td').eq(0).text() === student.studentId.toString();
	});

	if (row.length > 0) {
		row.find('td').eq(1).text(student.dni);
		row.find('td').eq(2).text(student.firstName);
		row.find('td').eq(3).text(student.lastName);
		row.find('td').eq(4).text(student.phone);
		row.find('td').eq(5).text(student.email);

		row.find('td').eq(6).html(student.status === 'activo'
			? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle p-1">Activo</span>'
			: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle p-1">Inactivo</span>');

		table.row(row).invalidate().draw();

		initializeTooltips(row);
	}
}

/*****************************************
 * FORM LOGIC
 *****************************************/

function handleAddStudentForm() {
	let isFirstSubmit = true;

	$('#addStudentModal').on('hidden.bs.modal', function() {
		isFirstSubmit = true;
		$('#addStudentForm').data("submitted", false);
	});

	$('#addStudentForm').on('input change', 'input, select', function() {
		if (!isFirstSubmit) {
			validateAddField($(this));
		}
	});

	$('#addStudentForm').on('submit', function(event) {
		event.preventDefault();

		if ($(this).data("submitted") === true) {
			return;
		}
		$(this).data("submitted", true);

		if (isFirstSubmit) {
			isFirstSubmit = false;
		}

		var form = $(this)[0];
		var isValid = true;

		$(form).find('input, select').not('.bootstrap-select input[type="search"]').each(function() {
			const field = $(this);
			const valid = validateAddField(field);
			if (!valid) {
				isValid = false;
			}
		});

		if (isValid) {
			var data = $(this).serialize() + '&type=create';

			var submitButton = $(this).find('[type="submit"]');
			submitButton.prop('disabled', true);
			$("#addStudentSpinner").removeClass("d-none");
			$("#addStudentIcon").addClass("d-none");

			$.ajax({
				url: '/bookstudio/StudentServlet',
				type: 'POST',
				data: data,
				dataType: 'json',
				success: function(response) {
					if (response && response.success) {
						addRowToTable(response.data);
						$('#addStudentModal').modal('hide');
						showToast('Estudiante agregado exitosamente.', 'success');
					} else {
						if (response.field) {
							setFieldError(response.field, response.message);
							$('#addStudentForm').data("submitted", false);
						}
						else {
							showToast(response.message, 'error');
							$('#addStudentModal').modal('hide');
						}
					}
				},
				error: function(xhr) {
					var errorMessage = (xhr.responseJSON && xhr.responseJSON.message)
						? xhr.responseJSON.message
						: 'Hubo un error al agregar el estudiante.';
					var errorField = xhr.responseJSON && xhr.responseJSON.field
						? xhr.responseJSON.field
						: null;

					if (errorField) {
						setFieldError(errorField, errorMessage);
						$('#addStudentForm').data("submitted", false);
					} else {
						showToast(errorMessage, 'error');
						$('#addStudentModal').modal('hide');
					}
				},
				complete: function() {
					$("#addStudentSpinner").addClass("d-none");
					$("#addStudentIcon").removeClass("d-none");
					submitButton.prop('disabled', false);
				}
			});
			function setFieldError(fieldId, message) {
				var field = $('#' + fieldId);
				field.addClass('is-invalid');
				field.siblings('.invalid-feedback').html(message).show();
			}
		} else {
			$(this).data("submitted", false);
		}
	});

	function validateAddField(field) {
		if (field.attr('type') === 'search') {
			return true;
		}

		var errorMessage = 'Este campo es obligatorio.';
		var isValid = true;

		// Default validation
		if (!field.val() || (field[0].checkValidity && !field[0].checkValidity())) {
			field.addClass('is-invalid');
			field.siblings('.invalid-feedback').html(errorMessage);
			isValid = false;
		} else {
			field.removeClass('is-invalid');
		}

		// DNI validation
		if (field.is('#addStudentDNI')) {
			const dni = field.val();
			const dniPattern = /^\d{8}$/;
			if (!dniPattern.test(dni)) {
				errorMessage = 'El DNI debe contener exactamente 8 dígitos numéricos.';
				isValid = false;
			}
		}

		// Name validation
		if (field.is('#addStudentFirstName')) {
			const firstName = field.val();

			if (firstName.length < 3) {
				errorMessage = 'El nombre debe tener al menos 3 caracteres.';
				isValid = false;
			}
		}

		// Last name validation
		if (field.is('#addStudentLastName')) {
			const lastName = field.val();

			if (lastName.length < 3) {
				errorMessage = 'El apellido debe tener al menos 3 caracteres.';
				isValid = false;
			}
		}

		// Address validation
		if (field.is('#addStudentAddress')) {
			const address = field.val();
			if (address.length < 5) {
				errorMessage = 'La dirección debe tener al menos 5 caracteres.';
				isValid = false;
			} else if (!/^[A-Za-z0-9\s,.\-#]+$/.test(address)) {
				errorMessage = 'La dirección solo puede contener letras, números y los caracteres especiales: ,.-#';
				isValid = false;
			}
		}

		// Phone validation
		if (field.is('#addStudentPhone')) {
			const phone = field.val();
			if (!/^[9]\d{8}$/.test(phone)) {
				errorMessage = 'El número de teléfono debe comenzar con 9 y tener exactamente 9 dígitos.';
				isValid = false;
			}
		}

		// Email validation
		if (field.is('#addStudentEmail')) {
			const email = field.val();
			const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

			if (!emailRegex.test(email)) {
				errorMessage = 'Por favor ingrese un correo electrónico válido.';
				isValid = false;
			}
		}

		// Birthdate validation
		if (field.is('#addStudentBirthDate')) {
			const birthDate = new Date(field.val());
			const today = new Date();
			if (birthDate > today) {
				errorMessage = 'La fecha de nacimiento no puede ser en el futuro.';
				isValid = false;
			}
		}

		// Select validation
		if (field.is('select')) {
			var container = field.closest('.bootstrap-select');
			container.toggleClass('is-invalid', field.hasClass('is-invalid'));
			container.siblings('.invalid-feedback').html(errorMessage);
		}

		if (!isValid) {
			field.addClass('is-invalid');
			field.siblings('.invalid-feedback').html(errorMessage).show();
		} else {
			field.removeClass('is-invalid');
			field.siblings('.invalid-feedback').hide();
		}

		return isValid;
	}
}

function handleEditStudentForm() {
	let isFirstSubmit = true;

	$('#editStudentModal').on('hidden.bs.modal', function() {
		isFirstSubmit = true;
		$('#editStudentForm').data("submitted", false);
	});

	$('#editStudentForm').on('input change', 'input, select', function() {
		if (!isFirstSubmit) {
			validateEditField($(this));
		}
	});

	$('#editStudentForm').on('submit', function(event) {
		event.preventDefault();

		if ($(this).data("submitted") === true) {
			return;
		}
		$(this).data("submitted", true);

		if (isFirstSubmit) {
			isFirstSubmit = false;
		}

		var form = $(this)[0];
		var isValid = true;

		$(form).find('input, select').not('.bootstrap-select input[type="search"]').each(function() {
			const field = $(this);
			const valid = validateEditField(field);
			if (!valid) {
				isValid = false;
			}
		});

		if (isValid) {
			var data = $(this).serialize() + '&type=update';

			var studentId = $(this).data('studentId');
			if (studentId) {
				data += '&studentId=' + encodeURIComponent(studentId);
			}

			var submitButton = $(this).find('[type="submit"]');
			submitButton.prop('disabled', true);
			$("#editStudentSpinner").removeClass("d-none");
			$("#editStudentIcon").addClass("d-none");

			$.ajax({
				url: '/bookstudio/StudentServlet',
				type: 'POST',
				data: data,
				dataType: 'json',
				success: function(response) {
					if (response && response.success) {
						updateRowInTable(response.data);
						$('#editStudentModal').modal('hide');
						showToast('Estudiante actualizado exitosamente.', 'success');
					} else {
						if (response.field) {
							setFieldError(response.field, response.message);
							$('#editStudentForm').data("submitted", false);
						}
						else {
							showToast(response.message, 'error');
							$('#editStudentModal').modal('hide');
						}
					}
				},
				error: function(xhr) {
					var errorMessage = (xhr.responseJSON && xhr.responseJSON.message)
						? xhr.responseJSON.message
						: 'Hubo un error al editar el estudiante.';
					var errorField = xhr.responseJSON && xhr.responseJSON.field
						? xhr.responseJSON.field
						: null;

					if (errorField) {
						setFieldError(errorField, errorMessage);
						$('#editStudentForm').data("submitted", false);
					} else {
						showToast(errorMessage, 'error');
						$('#editStudentModal').modal('hide');
					}
				},
				complete: function() {
					$("#editStudentSpinner").addClass("d-none");
					$("#editStudentIcon").removeClass("d-none");
					submitButton.prop('disabled', false);
				}
			});
			function setFieldError(fieldId, message) {
				var field = $('#' + fieldId);
				field.addClass('is-invalid');
				field.siblings('.invalid-feedback').html(message).show();
			}
		} else {
			$(this).data("submitted", false);
		}
	});
}

function validateEditField(field) {
	if (field.attr('type') === 'search') {
		return true;
	}

	var errorMessage = 'Este campo es obligatorio.';
	var isValid = true;

	// Default validation
	if (!field.val() || (field[0].checkValidity && !field[0].checkValidity())) {
		field.addClass('is-invalid');
		field.siblings('.invalid-feedback').html(errorMessage);
		isValid = false;
	} else {
		field.removeClass('is-invalid');
	}

	// Address validation
	if (field.is('#editStudentAddress')) {
		const address = field.val();
		if (address.length < 5) {
			errorMessage = 'La dirección debe tener al menos 5 caracteres.';
			isValid = false;
		} else if (!/^[A-Za-z0-9\s,.\-#]+$/.test(address)) {
			errorMessage = 'La dirección solo puede contener letras, números y los caracteres especiales: ,.-#';
			isValid = false;
		}
	}

	// Phone validation
	if (field.is('#editStudentPhone')) {
		const phone = field.val();
		if (!/^[9]\d{8}$/.test(phone)) {
			errorMessage = 'El número de teléfono debe comenzar con 9 y tener exactamente 9 dígitos.';
			isValid = false;
		}
	}

	// Email validation
	if (field.is('#editStudentEmail')) {
		const email = field.val();
		const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

		if (!emailRegex.test(email)) {
			errorMessage = 'Por favor ingrese un correo electrónico válido.';
			isValid = false;
		}
	}

	// Select validation
	if (field.is('select')) {
		var container = field.closest('.bootstrap-select');
		container.toggleClass('is-invalid', field.hasClass('is-invalid'));
		container.siblings('.invalid-feedback').html('Opción seleccionada inactiva o no existente.');
	}

	if (!isValid) {
		field.addClass('is-invalid');
		field.siblings('.invalid-feedback').html(errorMessage).show();
	} else {
		field.removeClass('is-invalid');
		field.siblings('.invalid-feedback').hide();
	}

	return isValid;
}

/*****************************************
 * MODAL MANAGEMENT
 *****************************************/

function loadModalData() {
	// Add Modal
	$(document).on('click', '[data-bs-target="#addStudentModal"]', function() {
		$('#addStudentGender').selectpicker('destroy').empty().append(
			$('<option>', {
				value: 'Masculino',
				text: 'Masculino'
			}),
			$('<option>', {
				value: 'Femenino',
				text: 'Femenino'
			})
		);
		$('#addStudentGender').selectpicker();

		populateSelect('#addStudentFaculty', facultyList, 'facultyId', 'facultyName');
		$('#addStudentFaculty').selectpicker();

		$('#addStudentStatus').selectpicker('destroy').empty().append(
			$('<option>', {
				value: 'activo',
				text: 'Activo'
			}),
			$('<option>', {
				value: 'inactivo',
				text: 'Inactivo'
			})
		);
		$('#addStudentStatus').selectpicker();

		$('#addStudentForm')[0].reset();
		$('#addStudentForm .is-invalid').removeClass('is-invalid');

		placeholderColorDateInput();
	});

	// Details Modal
	$(document).on('click', '[data-bs-target="#detailsStudentModal"]', function() {
		var studentId = $(this).data('id');

		$.ajax({
			url: '/bookstudio/StudentServlet',
			type: 'GET',
			data: { type: 'details', studentId: studentId },
			dataType: 'json',
			success: function(data) {
				$('#detailsStudentID').text(data.studentId);
				$('#detailsStudentDNI').text(data.dni);
				$('#detailsStudentFirstName').text(data.firstName);
				$('#detailsStudentLastName').text(data.lastName);
				$('#detailsStudentAddress').text(data.address);
				$('#detailsStudentPhone').text(data.phone);
				$('#detailsStudentEmail').text(data.email);
				$('#detailsStudentBirthDate').text(moment(data.birthDate).format('DD/MM/YYYY'));
				$('#detailsStudentGender').text(data.gender);
				$('#detailsStudentFaculty').text(data.facultyName);
				$('#detailsStudentStatus').html(
					data.status === 'activo'
						? '<span class="badge bg-success p-1">Activo</span>'
						: '<span class="badge bg-danger p-1">Inactivo</span>'
				);
			},
			error: function(status, error) {
				console.log("Error al cargar los detalles del estudiante:", status, error);
			}
		});
	});

	// Edit Modal
	$(document).on('click', '[data-bs-target="#editStudentModal"]', function() {
		var studentId = $(this).data('id');

		$.ajax({
			url: '/bookstudio/StudentServlet',
			type: 'GET',
			data: { type: 'details', studentId: studentId },
			dataType: 'json',
			success: function(data) {
				$('#editStudentForm').data('studentId', data.studentId);
				$('#editStudentDNI').val(data.dni);
				$('#editStudentFirstName').val(data.firstName);
				$('#editStudentLastName').val(data.lastName);
				$('#editStudentAddress').val(data.address);
				$('#editStudentPhone').val(data.phone);
				$('#editStudentEmail').val(data.email);
				$('#editStudentBirthDate').val(moment(data.birthDate).format('YYYY-MM-DD'));

				$('#editStudentGender').selectpicker('destroy').empty().append(
					$('<option>', {
						value: 'Masculino',
						text: 'Masculino'
					}),
					$('<option>', {
						value: 'Femenino',
						text: 'Femenino'
					})
				);
				$('#editStudentGender').val(data.gender);
				$('#editStudentGender').selectpicker();

				populateSelect('#editStudentFaculty', facultyList, 'facultyId', 'facultyName');
				$('#editStudentFaculty').val(data.facultyId);
				$('#editStudentFaculty').selectpicker();

				$('#editStudentStatus').selectpicker('destroy').empty().append(
					$('<option>', {
						value: 'activo',
						text: 'Activo'
					}),
					$('<option>', {
						value: 'inactivo',
						text: 'Inactivo'
					})
				);
				$('#editStudentStatus').val(data.status);
				$('#editStudentStatus').selectpicker();

				$('#editStudentForm .is-invalid').removeClass('is-invalid');

				placeholderColorEditSelect();
				placeholderColorDateInput();

				$('#editStudentForm').find('select').each(function() {
					validateEditField($(this), true);
				});
			},
			error: function(status, error) {
				console.log("Error al cargar los detalles del estudiante para editar:", status, error);
			}
		});
	});
}

function setupBootstrapSelectDropdownStyles() {
	const observer = new MutationObserver((mutationsList) => {
		mutationsList.forEach((mutation) => {
			mutation.addedNodes.forEach((node) => {
				if (node.nodeType === 1 && node.classList.contains('dropdown-menu')) {
					const $dropdown = $(node);
					$dropdown.addClass('gap-1 px-2 rounded-3 mx-0 shadow');
					$dropdown.find('.dropdown-item').addClass('rounded-2 d-flex align-items-center justify-content-between'); // Alineación

					$dropdown.find('li:not(:first-child)').addClass('mt-1');

					updateDropdownIcons($dropdown);
				}
			});
		});
	});

	observer.observe(document.body, { childList: true, subtree: true });

	$(document).on('click', '.bootstrap-select .dropdown-item', function() {
		const $dropdown = $(this).closest('.dropdown-menu');
		updateDropdownIcons($dropdown);
	});
}

function updateDropdownIcons($dropdown) {
	$dropdown.find('.dropdown-item').each(function() {
		const $item = $(this);
		let $icon = $item.find('i.bi-check2');

		if ($item.hasClass('active') && $item.hasClass('selected')) {
			if ($icon.length === 0) {
				$('<i class="bi bi-check2 ms-auto"></i>').appendTo($item);
			}
		} else {
			$icon.remove();
		}
	});
}

function initializeTooltips(container) {
	$(container).find('[data-tooltip="tooltip"]').tooltip({
		trigger: 'hover'
	}).on('click', function() {
		$(this).tooltip('hide');
	});
}

function generatePDF(dataTable) {
	const { jsPDF } = window.jspdf;
	const doc = new jsPDF("l", "mm", "a4");
	const logoUrl = '/bookstudio/images/bookstudio-logo-no-bg.png';

	const currentDate = new Date();
	const fecha = currentDate.toLocaleDateString('es-ES', {
		day: '2-digit',
		month: '2-digit',
		year: 'numeric'
	});
	const hora = currentDate.toLocaleTimeString('en-US', {
		hour: '2-digit',
		minute: '2-digit',
		hour12: true
	});

	const pageWidth = doc.internal.pageSize.getWidth();
	const margin = 10;
	const topMargin = 5;

	doc.addImage(logoUrl, 'PNG', margin, topMargin - 5, 30, 30);
	doc.setFont("helvetica", "bold");
	doc.setFontSize(14);
	doc.text("Lista de Estudiantes", pageWidth / 2, topMargin + 13, { align: "center" });

	doc.setFont("helvetica", "normal");
	doc.setFontSize(8);
	doc.text(`Fecha: ${fecha}`, pageWidth - margin, topMargin + 10, { align: "right" });
	doc.text(`Hora: ${hora}`, pageWidth - margin, topMargin + 15, { align: "right" });

	const data = dataTable.rows({ search: 'applied' }).nodes().toArray().map(row => {
		let estado = row.cells[6].innerText.trim();
		estado = estado.includes("Activo") ? "Activo" : "Inactivo";

		return [
			row.cells[0].innerText.trim(),
			row.cells[1].innerText.trim(),
			row.cells[2].innerText.trim(),
			row.cells[3].innerText.trim(),
			row.cells[4].innerText.trim(),
			row.cells[5].innerText.trim(),
			estado
		];
	});

	doc.autoTable({
		startY: topMargin + 25,
		margin: { left: margin, right: margin },
		head: [['ID', 'DNI', 'Nombres', 'Apellidos', 'Teléfono', 'Correo electrónico', 'Estado']],
		body: data,
		theme: 'grid',
		headStyles: {
			fillColor: [0, 0, 0],
			textColor: 255,
			fontStyle: 'bold',
			fontSize: 8,
			halign: 'left'
		},
		bodyStyles: {
			font: "helvetica",
			fontSize: 7,
			halign: 'left'
		},
		didParseCell: function(data) {
			if (data.section === 'body' && data.column.index === 6) {
				data.cell.styles.textColor = data.cell.raw === "Activo"
					? [0, 128, 0]
					: [255, 0, 0];
			}
		}
	});

	const filename = `Lista_de_Estudiantes_BookStudio_${fecha.replace(/\//g, '-')}.pdf`;

	const pdfBlob = doc.output('blob');
	const blobUrl = URL.createObjectURL(pdfBlob);
	const link = document.createElement('a');
	link.href = blobUrl;
	link.download = filename;
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}

function generateExcel(dataTable) {
	const workbook = new ExcelJS.Workbook();
	const worksheet = workbook.addWorksheet('Estudiantes');

	const currentDate = new Date();
	const dateStr = currentDate.toLocaleDateString('es-ES', {
		day: '2-digit',
		month: '2-digit',
		year: 'numeric'
	});
	const timeStr = currentDate.toLocaleTimeString('en-US', {
		hour: '2-digit',
		minute: '2-digit',
		hour12: true
	});

	worksheet.mergeCells('A1:G1');
	const titleCell = worksheet.getCell('A1');
	titleCell.value = 'Lista de Estudiantes - BookStudio';
	titleCell.font = { name: 'Arial', size: 14, bold: true };
	titleCell.alignment = { horizontal: 'center' };

	worksheet.mergeCells('A2:G2');
	const dateTimeCell = worksheet.getCell('A2');
	dateTimeCell.value = `Fecha: ${dateStr}  Hora: ${timeStr}`;
	dateTimeCell.alignment = { horizontal: 'center' };

	worksheet.columns = [
		{ key: 'id', width: 10 },
		{ key: 'dni', width: 15 },
		{ key: 'nombres', width: 30 },
		{ key: 'apellidos', width: 30 },
		{ key: 'telefono', width: 20 },
		{ key: 'correo', width: 30 },
		{ key: 'estado', width: 15 }
	];

	const headerRow = worksheet.getRow(4);
	headerRow.values = ['ID', 'DNI', 'Nombres', 'Apellidos', 'Teléfono', 'Correo electrónico', 'Estado'];
	headerRow.eachCell((cell) => {
		cell.font = { bold: true, color: { argb: 'FFFFFF' } };
		cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '000000' } };
		cell.alignment = { horizontal: 'left', vertical: 'middle' };
		cell.border = {
			top: { style: 'thin', color: { argb: 'FFFFFF' } },
			bottom: { style: 'thin', color: { argb: 'FFFFFF' } },
			left: { style: 'thin', color: { argb: 'FFFFFF' } },
			right: { style: 'thin', color: { argb: 'FFFFFF' } }
		};
	});

	const data = dataTable.rows({ search: 'applied' }).nodes().toArray().map(row => {
		let estado = row.cells[6].innerText.trim();
		estado = estado.includes("Activo") ? "Activo" : "Inactivo";

		return {
			id: row.cells[0].innerText.trim(),
			dni: row.cells[1].innerText.trim(),
			nombres: row.cells[2].innerText.trim(),
			apellidos: row.cells[3].innerText.trim(),
			telefono: row.cells[4].innerText.trim(),
			correo: row.cells[5].innerText.trim(),
			estado: estado
		};
	});

	data.forEach((item) => {
		const row = worksheet.addRow(item);
		const estadoCell = row.getCell(7);
		if (estadoCell.value === "Activo") {
			estadoCell.font = { color: { argb: '008000' } };
			estadoCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'E6F2E6' } };
		} else {
			estadoCell.font = { color: { argb: 'FF0000' } };
			estadoCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFE6E6' } };
		}
	});

	const filename = `Lista_de_Estudiantes_BookStudio_${dateStr.replace(/\//g, '-')}.xlsx`;

	workbook.xlsx.writeBuffer().then(buffer => {
		const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
		const link = document.createElement('a');
		link.href = URL.createObjectURL(blob);
		link.download = filename;
		link.click();
	});
}

/*****************************************
 * INITIALIZATION
 *****************************************/

$(document).ready(function() {
	loadStudents();
	handleAddStudentForm();
	handleEditStudentForm();
	loadModalData();
	populateSelectOptions();
	$('.selectpicker').selectpicker();
	setupBootstrapSelectDropdownStyles();
	placeholderColorSelect();
});