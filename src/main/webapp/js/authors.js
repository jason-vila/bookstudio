/**
 * authors.js
 * Manages the initialization, data loading, and configuration of the authors table,  
 * as well as handling modals for creating, viewing, editing author details, 
 * and performing logical delete (status change) operations on authors.
 * Utilizes AJAX for CRUD operations on author data.
 * Includes functions to manage UI elements like placeholders, dropdown styles, and tooltips.
 * 
 * @author [Jason]
 */

/*****************************************
 * GLOBAL VARIABLES AND HELPER FUNCTIONS
 *****************************************/

// Global list of literary genres for the selectpickers
var literaryGenreList = [];

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
        url: '/bookstudio/AuthorServlet',
        type: 'GET',
        data: { type: 'populateSelects' },
        dataType: 'json',
        success: function (data) {
            if (data) {
                literaryGenreList = data.literaryGenres;

				populateSelect('#addLiteraryGenre', literaryGenreList, 'literaryGenreId', 'genreName');
				
                populateSelect('#editLiteraryGenre', literaryGenreList, 'literaryGenreId', 'genreName');
            }
        },
        error: function (status, error) {
            console.error("Error al obtener los datos para los select:", status, error);
        }
    });
}

function placeholderColorSelect() {	
	$('select.selectpicker').on('change', function () {
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
	$('select[id^="edit"]').each(function () {
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

function generateRow(author) {
    return `
        <tr>
            <td class="align-middle text-start">${author.authorId}</td>
            <td class="align-middle text-start">${author.name}</td>
            <td class="align-middle text-start">${author.nationality}</td>
			<td class="align-middle text-start">${author.literaryGenreName}</td>
            <td class="align-middle text-center">
                ${author.status === 'activo' 
					? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle p-1">Activo</span>' 
					: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle p-1">Inactivo</span>'}
            </td>
			<td class="align-middle text-center">
                ${author.photoBase64 ? 
                    `<img src="${author.photoBase64}" alt="Foto" class="img-fluid rounded-circle" style="width: 25px; height: 25px;">` : 
                    `<a href="#" class="text-muted">#</a>`}
            </td>
            <td class="align-middle text-center">
                <div class="d-inline-flex gap-2">
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Detalles"
					    data-bs-toggle="modal" data-bs-target="#detailsAuthorModal" data-id="${author.authorId}">
					    <i class="bi bi-eye"></i>
					</button>
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Editar"
					    data-bs-toggle="modal" data-bs-target="#editAuthorModal" data-id="${author.authorId}">
					    <i class="bi bi-pencil"></i>
					</button>
                </div>
            </td>
        </tr>
    `;
}

function addRowToTable(author) {
	var table = $('#authorTable').DataTable();
    var rowHtml = generateRow(author);
    var $row = $(rowHtml);

    table.row.add($row).draw();

    initializeTooltips($row);
}

function loadAuthors() {
    toggleButtonAndSpinner('loading');
    
    let safetyTimer = setTimeout(function() {
        toggleButtonAndSpinner('loaded');
        $('#tableContainer').removeClass('d-none');
        $('#cardContainer').removeClass('h-100');
    }, 8000);
    
    $.ajax({
        url: '/bookstudio/AuthorServlet',
        type: 'GET',
        data: { type: 'list' },
        dataType: 'json',
        success: function(data) {
            clearTimeout(safetyTimer);
            
            var tableBody = $('#bodyAuthors');
            tableBody.empty();
            
            if (data && data.length > 0) {
                data.forEach(function(author) {
                    var row = generateRow(author);
                    tableBody.append(row);
                });
                
                initializeTooltips(tableBody);
            }
            
            if ($.fn.DataTable.isDataTable('#authorTable')) {
                $('#authorTable').DataTable().destroy();
            }
            
            setupDataTable('#authorTable');
        },
        error: function(status, error) {
            clearTimeout(safetyTimer);
            console.error("Error en la solicitud AJAX:", status, error);
            
            var tableBody = $('#bodyAuthors');
            tableBody.empty();
            
            if ($.fn.DataTable.isDataTable('#authorTable')) {
                $('#authorTable').DataTable().destroy();
            }
            
            setupDataTable('#authorTable');
        }
    });
}

function updateRowInTable(author) {
    var table = $('#authorTable').DataTable();

    var row = table.rows().nodes().to$().filter(function() {
        return $(this).find('td').eq(0).text() === author.authorId.toString();
    });

    if (row.length > 0) {
        row.find('td').eq(1).text(author.name);
        row.find('td').eq(2).text(author.nationality);
        row.find('td').eq(3).text(author.literaryGenreName);
        row.find('td').eq(4).html(author.status === 'activo' 
			? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle p-1">Activo</span>' 
			: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle p-1">Inactivo</span>');
			
		if (author.photoBase64 && author.photoBase64.trim() !== "") {
            row.find('td').eq(5).html(`<img src="${author.photoBase64}" alt="Foto" class="img-fluid rounded-circle" style="width: 25px; height: 25px;">`);
        } else {
            if (author.photoBase64 === undefined || author.photoBase64.trim() === "") {
                if (!row.find('td').eq(5).find('img').length) {
                    row.find('td').eq(5).html('<a href="#" class="text-muted">#</a>');
                }
            }
        }
        
        table.row(row).invalidate().draw();
		
		initializeTooltips(row);
    }
}

/*****************************************
 * FORM LOGIC
 *****************************************/

function handleAddAuthorForm() {
	let isFirstSubmit = true;
				
	$('#addAuthorModal').on('hidden.bs.modal', function () {
        isFirstSubmit = true;
		$('#addAuthorForm').data("submitted", false);
    });
	
	$('#addAuthorForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
	        validateAddField($(this));
	    }
    });
	
    $('#addAuthorForm').on('submit', function (event) {
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

        $(form).find('input, select').not('.bootstrap-select input[type="search"]').each(function () {
            const field = $(this);
            const valid = validateAddField(field);
            if (!valid) {
                isValid = false;
            }
        });

		if (isValid) {
			var formData = new FormData(this);
			
			var submitButton = $(this).find('[type="submit"]');
            submitButton.prop('disabled', true);
            $("#addAuthorSpinner").removeClass("d-none");
            $("#addAuthorIcon").addClass("d-none");
					
			if (cropper) {
	            cropper.getCroppedCanvas({
	                width: 460,
	                height: 460
	            }).toBlob(function (blob) {
	                formData.set('addAuthorPhoto', blob, 'photo.png');
	                sendAddForm(formData);
	            }, 'image/png');
		    } else {
				sendAddForm(formData);
			}
			
			function sendAddForm(formData) {
				formData.append('type', 'create');
				
				$.ajax({
		            url: '/bookstudio/AuthorServlet',
		            type: 'POST',
		            data: formData,
		            dataType: 'json',
					processData: false,
					contentType: false,
		            success: function (response) {
		                if (response && response.success) {
		                    addRowToTable(response.data);
		                    $('#addAuthorModal').modal('hide');
		                    showToast('Autor agregado exitosamente.', 'success');
		                } else {
							$('#addAuthorModal').modal('hide');
		                    showToast('Hubo un error al agregar el autor.', 'error');
		                }
		            },
		            error: function () {
		                $('#addAuthorModal').modal('hide');
		                showToast('Hubo un error al agregar el autor.', 'error');
		            },
					complete: function() {
                        $("#addAuthorSpinner").addClass("d-none");
                        $("#addAuthorIcon").removeClass("d-none");
                        submitButton.prop('disabled', false);
                    }
		        });
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
		
		// Name validation
		if (field.is('#addAuthorName')) {
		    const firstName = field.val();

		    if (firstName.length < 3) {
		        errorMessage = 'El nombre debe tener al menos 3 caracteres.';
		        isValid = false;
		    }
		}
		
		// Birth date validation
		if (field.is('#addAuthorBirthDate')) {
		    const birthDate = new Date(field.val());
		    const today = new Date();
		    const minAge = 10;
		    const minDate = new Date(today.getFullYear() - minAge, today.getMonth(), today.getDate());

		    if (birthDate > today) {
		        errorMessage = 'La fecha de nacimiento no puede ser en el futuro.';
		        isValid = false;
		    } else if (birthDate > minDate) {
		        errorMessage = `El autor debe tener al menos ${minAge} años.`;
		        isValid = false;
		    }
		}
		
		// Photo validation
	    if (field.is('#addAuthorPhoto')) {
	        var file = field[0].files[0];

	        if (!file) {
	            field.removeClass('is-invalid');
	            return true;
	        }

	        var validExtensions = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

	        if (!validExtensions.includes(file.type)) {
	            isValid = false;
	            errorMessage = 'Solo se permiten imágenes en formato JPG, PNG, GIF o WEBP.';
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

$('#addAuthorPhoto, #editAuthorPhoto').on('change', function () {
    var fileInput = $(this);
    var file = fileInput[0].files[0];

    if (file) {
        var validExtensions = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (!validExtensions.includes(file.type)) {
            fileInput.addClass('is-invalid');
            fileInput.siblings('.invalid-feedback').html('Solo se permiten imágenes en formato JPG, PNG, GIF o WEBP.');
        } else {
            fileInput.removeClass('is-invalid');
        }
    } else {
        fileInput.removeClass('is-invalid');
    }
});

function handleEditAuthorForm() {
	let isFirstSubmit = true;
				
	$('#editAuthorModal').on('hidden.bs.modal', function () {
        isFirstSubmit = true;
		$('#editAuthorForm').data("submitted", false);
    });
		
	$('#editAuthorForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
	        validateEditField($(this));
	    }
    });
	
    $('#editAuthorForm').on('submit', function(event) {
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

		$(form).find('input, select').not('.bootstrap-select input[type="search"]').each(function () {
            const field = $(this);
            const valid = validateEditField(field);
            if (!valid) {
                isValid = false;
            }
        });
		
		if (isValid) {
			var formData = new FormData(this);
			
			var authorId = $(this).data('authorId');
            if (authorId) {
                formData.append('authorId', authorId);
            }
			
			var submitButton = $(this).find('[type="submit"]');
			submitButton.prop('disabled', true);
			$("#editAuthorSpinner").removeClass("d-none");
			$("#editAuthorIcon").addClass("d-none");

			if (cropper) {
	            cropper.getCroppedCanvas({
	                width: 460,
	                height: 460
	            }).toBlob(function (blob) {
	                formData.set('editAuthorPhoto', blob, 'photo.png');
	                sendEditForm(formData);
	            }, 'image/png');
	        } else {
	            sendEditForm(formData);
	        }
			
			function sendEditForm(formData) {
				formData.append('type', 'update');
				
				$.ajax({
		            url: '/bookstudio/AuthorServlet',
		            type: 'POST',
		            data: formData,
		            dataType: 'json',
		            processData: false,
		            contentType: false,
		            success: function(response) {
		                if (response.success) {
		                    updateRowInTable(response.data);
		                    $('#editAuthorModal').modal('hide');
		                    showToast('Autor actualizado exitosamente.', 'success');
		                } else {
							$('#editAuthorModal').modal('hide');
		                    showToast('Hubo un error al actualizar el autor.', 'error');
		                }
		            },
		            error: function() {
		                $('#editAuthorModal').modal('hide');
		                showToast('Hubo un error al actualizar el autor.', 'error');
		            },
					complete: function() {
						$("#editAuthorSpinner").addClass("d-none");
						$("#editAuthorIcon").removeClass("d-none");
                        submitButton.prop('disabled', false);
                    }
		        });
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
	
	// Name validation
	if (field.is('#editAuthorName')) {
	    const firstName = field.val();

	    if (firstName.length < 3) {
	        errorMessage = 'El nombre debe tener al menos 3 caracteres.';
	        isValid = false;
	    }
	}
	
	// Birth date validation
	if (field.is('#editAuthorBirthDate')) {
	    const birthDate = new Date(field.val());
	    const today = new Date();
	    const minAge = 10;
	    const minDate = new Date(today.getFullYear() - minAge, today.getMonth(), today.getDate());

	    if (birthDate > today) {
	        errorMessage = 'La fecha de nacimiento no puede ser en el futuro.';
	        isValid = false;
	    } else if (birthDate > minDate) {
	        errorMessage = `El autor debe tener al menos ${minAge} años.`;
	        isValid = false;
	    }
	}
	
	// Photo validation
    if (field.is('#editAuthorPhoto')) {
        var file = field[0].files[0];

        if (!file) {
            field.removeClass('is-invalid');
            return true;
        }

        var validExtensions = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

        if (!validExtensions.includes(file.type)) {
            isValid = false;
            errorMessage = 'Solo se permiten imágenes en formato JPG, PNG, GIF o WEBP.';
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
    $(document).on('click', '[data-bs-target="#addAuthorModal"]', function () {
		populateSelect('#addLiteraryGenre', literaryGenreList, 'literaryGenreId', 'genreName');	
		$('#addLiteraryGenre').selectpicker();
		
		$('#addAuthorStatus').selectpicker('destroy').empty().append(
            $('<option>', {
                value: 'activo',
                text: 'Activo'
            }),
            $('<option>', {
                value: 'inactivo',
                text: 'Inactivo'
            })
        );
		$('#addAuthorStatus').selectpicker();
		
		$('#addAuthorForm')[0].reset();
		$('#addAuthorForm .is-invalid').removeClass('is-invalid');
		
		placeholderColorDateInput();
		
		$('#cropperContainerAdd').addClass('d-none');
		if (cropper) {
		    cropper.destroy();
		    cropper = null;
		}
    });

    // Details Modal
    $(document).on('click', '[data-bs-target="#detailsAuthorModal"]', function() {
        var authorId = $(this).data('id');

        $.ajax({
            url: '/bookstudio/AuthorServlet',
            type: 'GET',
            data: { type: 'details', authorId: authorId },
            dataType: 'json',
            success: function(data) {
                $('#detailsAuthorID').text(data.authorId);
                $('#detailsAuthorName').text(data.name);
                $('#detailsAuthorNationality').text(data.nationality);
				$('#detailsAuthorGenre').text(data.literaryGenreName);							
				$('#detailsAuthorBirthDate').text(moment(data.birthDate).format('DD/MM/YYYY'));
				$('#detailsAuthorBiography').text(data.biography);
				$('#detailsAuthorStatus').html(
				    data.status === 'activo' 
				        ? '<span class="badge bg-success p-1">Activo</span>' 
				        : '<span class="badge bg-danger p-1">Inactivo</span>'
				);
				if (data.photoBase64) {
	                $('#detailsAuthorPhoto').attr('src', data.photoBase64);
	            } else {
	                $('#detailsAuthorPhoto').attr('src', '#');
	            }
            },
            error: function(status, error) {
                console.log("Error al cargar los detalles del autor:", status, error);
            }
        });
    });
	
    // Edit Modal
    $(document).on('click', '[data-bs-target="#editAuthorModal"]', function() {
        var authorId = $(this).data('id');

        $.ajax({
            url: '/bookstudio/AuthorServlet',
            type: 'GET',
            data: { type: 'details', authorId: authorId },
            dataType: 'json',
            success: function(data) {
				$('#editAuthorForm').data('authorId', data.authorId);
				
				$('#editAuthorName').val(data.name);
				$('#editAuthorNationality').val(data.nationality);
				
				populateSelect('#editLiteraryGenre', literaryGenreList, 'literaryGenreId', 'genreName');
                $('#editLiteraryGenre').val(data.literaryGenreId);
                $('#editLiteraryGenre').selectpicker();
				
                $('#editAuthorBirthDate').val(moment(data.birthDate).format('YYYY-MM-DD'));	
				$('#editAuthorBiography').val(data.biography);
				
				$('#editAuthorStatus').selectpicker('destroy').empty().append(
	                $('<option>', {
	                    value: 'activo',
	                    text: 'Activo'
	                }),
	                $('<option>', {
	                    value: 'inactivo',
	                    text: 'Inactivo'
	                })
	            );
                $('#editAuthorStatus').val(data.status);
				$('#editAuthorStatus').selectpicker();
				
				$('#editAuthorForm .is-invalid').removeClass('is-invalid');
				
				placeholderColorEditSelect();
				placeholderColorDateInput();
				
				$('#editAuthorForm').find('select').each(function() {
		            validateEditField($(this), true);
		        });
				
				$('#editAuthorPhoto').val('');
            },
            error: function(status, error) {
                console.log("Error al cargar los detalles del autor para editar:", status, error);
            }
        });
		
		$('#cropperContainerEdit').addClass('d-none');
						
		if (cropper) {
	        cropper.destroy();
	        cropper = null;
	    }
    });
}

let cropper;
const $cropperContainerAdd = $('#cropperContainerAdd');
const $imageToCropAdd = $('#imageToCropAdd');
const $cropperContainerEdit = $('#cropperContainerEdit');
const $imageToCropEdit = $('#imageToCropEdit');

function initializeCropper(file, $cropperContainer, $imageToCrop) {
    const reader = new FileReader();
    reader.onload = function(e) {
        $cropperContainer.removeClass('d-none');
        $imageToCrop.attr('src', e.target.result);

        if (cropper) {
            cropper.destroy();
        }

        cropper = new Cropper($imageToCrop[0], {
            aspectRatio: 1,
            viewMode: 1,
            autoCropArea: 1,
            responsive: true,
            checkOrientation: false,
            ready: function() {
                $('.cropper-crop-box').css({
                    'border-radius': '50%',
                    'overflow': 'hidden'
                });
            }
        });
    };
    reader.readAsDataURL(file);
}

$('#addAuthorPhoto, #editAuthorPhoto').on('change', function () {
    const file = this.files[0];

    if (file && file.type.startsWith('image/')) {
        let $container, $image;
        if ($(this).is('#addAuthorPhoto')) {
            $container = $cropperContainerAdd;
            $image = $imageToCropAdd;
        } else {
            $container = $cropperContainerEdit;
            $image = $imageToCropEdit;
        }
        initializeCropper(file, $container, $image);
    } else {
        if ($(this).is('#addAuthorPhoto')) {
            $cropperContainerAdd.addClass('d-none');
        } else {
            $cropperContainerEdit.addClass('d-none');
        }
    }
});

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

    $(document).on('click', '.bootstrap-select .dropdown-item', function () {
        const $dropdown = $(this).closest('.dropdown-menu');
        updateDropdownIcons($dropdown);
    });
}

function updateDropdownIcons($dropdown) {
    $dropdown.find('.dropdown-item').each(function () {
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

/*****************************************
 * INITIALIZATION
 *****************************************/

$(document).ready(function() {
    loadAuthors();
    handleAddAuthorForm();
	handleEditAuthorForm();
    loadModalData();
	populateSelectOptions();
    $('.selectpicker').selectpicker();
	setupBootstrapSelectDropdownStyles();
	placeholderColorSelect();
});