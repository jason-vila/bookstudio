/**
 * users.js
 * Manages the initialization, data loading, and configuration of the users table,  
 * as well as handling modals for creating, viewing, editing, and deleting user details.  
 * Utilizes AJAX for CRUD operations on user data.  
 * Includes functions to manage UI elements like placeholders, dropdown styles, and tooltips.  
 * 
 * @author [Jason]
 */

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

/*****************************************
 * TABLE HANDLING
 *****************************************/

function generateRow(user) {
    return `
        <tr>
            <td class="align-middle text-start">${user.userId}</td>
            <td class="align-middle text-start">${user.username}</td>
            <td class="align-middle text-start">${user.email}</td>
			<td class="align-middle text-start">${user.firstName}</td>
			<td class="align-middle text-start">${user.lastName}</td>
			<td class="align-middle text-start">${user.role.charAt(0).toUpperCase() + user.role.slice(1)}</td>
			<td class="align-middle text-center">
                ${user.profilePhotoBase64 ? 
                    `<img src="${user.profilePhotoBase64}" alt="Foto" class="img-fluid rounded-circle" style="width: 25px; height: 25px;">` : 
                    `<a href="#" class="text-muted">#</a>`}
            </td>
            <td class="align-middle text-center">
                <div class="d-inline-flex gap-2">
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Detalles"
					    data-bs-toggle="modal" data-bs-target="#detailsUserModal" data-id="${user.userId}">
					    <i class="bi bi-eye"></i>
					</button>
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Editar"
					    data-bs-toggle="modal" data-bs-target="#editUserModal" data-id="${user.userId}">
					    <i class="bi bi-pencil"></i>
					</button>
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Eliminar"
					    data-bs-toggle="modal" data-bs-target="#deleteUserModal" data-id="${user.userId}">
					    <i class="bi bi-trash"></i>
					</button>
                </div>
            </td>
        </tr>
    `;
}

function addRowToTable(user) {
	var table = $('#userTable').DataTable();
    var rowHtml = generateRow(user);
    var $row = $(rowHtml);

    table.row.add($row).draw();

    initializeTooltips($row);
}

function loadUsers() {
    toggleButtonAndSpinner('loading');
    
    let safetyTimer = setTimeout(function() {
        toggleButtonAndSpinner('loaded');
        $('#tableContainer').removeClass('d-none');
        $('#cardContainer').removeClass('h-100');
    }, 8000);
    
    $.ajax({
        url: '/bookstudio/UserServlet',
        type: 'GET',
        data: { type: 'list' },
        dataType: 'json',
        success: function(data) {
            clearTimeout(safetyTimer);
            
            var tableBody = $('#bodyUsers');
            tableBody.empty();
            
            if (data && data.length > 0) {
                data.forEach(function(user) {
                    var row = generateRow(user);
                    tableBody.append(row);
                });
                
                initializeTooltips(tableBody);
            }
            
            if ($.fn.DataTable.isDataTable('#userTable')) {
                $('#userTable').DataTable().destroy();
            }
            
            setupDataTable('#userTable');
        },
        error: function(status, error) {
            clearTimeout(safetyTimer);
            console.error("Error en la solicitud AJAX:", status, error);
            
            var tableBody = $('#bodyUsers');
            tableBody.empty();
            
            if ($.fn.DataTable.isDataTable('#userTable')) {
                $('#userTable').DataTable().destroy();
            }
            
            setupDataTable('#userTable');
        }
    });
}

function updateRowInTable(user) {
    var table = $('#userTable').DataTable();

    var row = table.rows().nodes().to$().filter(function() {
        return $(this).find('td').eq(0).text() === user.userId.toString();
    });

    if (row.length > 0) {
        row.find('td').eq(1).text(user.username);
        row.find('td').eq(2).text(user.email);
        row.find('td').eq(3).text(user.firstName);
        row.find('td').eq(4).text(user.lastName);
		row.find('td').eq(5).text(user.role.charAt(0).toUpperCase() + user.role.slice(1));

        if (user.profilePhotoBase64 && user.profilePhotoBase64.trim() !== "") {
            row.find('td').eq(6).html(`<img src="${user.profilePhotoBase64}" alt="Foto" class="img-fluid rounded-circle" style="width: 25px; height: 25px;">`);
        } else {
            if (user.profilePhotoBase64 === undefined || user.profilePhotoBase64.trim() === "") {
                if (!row.find('td').eq(6).find('img').length) {
                    row.find('td').eq(6).html('<a href="#" class="text-muted">#</a>');
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

function handleAddUserForm() {
	let isFirstSubmit = true;
	
	$('#addUserModal').on('hidden.bs.modal', function () {
        isFirstSubmit = true;
		$('#addUserForm').data("submitted", false);
    });
	
	$('#addUserForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
	        validateAddField($(this));
	    }
    });
	
    $('#addUserForm').on('submit', function (event) {
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
			$("#addUserSpinner").removeClass("d-none");
			$("#addUserIcon").addClass("d-none");
					
			if (cropper) {
	            cropper.getCroppedCanvas({
	                width: 460,
	                height: 460
	            }).toBlob(function (blob) {
	                formData.set('addUserProfilePhoto', blob, 'photo.png');
	                sendAddForm(formData);
	            }, 'image/png');	
		    } else {
				sendAddForm(formData);
			}
			
			function sendAddForm(formData) {
				formData.append('type', 'create');
				
				$.ajax({
		            url: '/bookstudio/UserServlet',
		            type: 'POST',
		            data: formData,
		            dataType: 'json',
					processData: false,
					contentType: false,
		            success: function (response) {
		                if (response && response.success) {
		                    addRowToTable(response.data);
		                    $('#addUserModal').modal('hide');
		                    showToast('Usuario agregado exitosamente.', 'success');
		                } else {
							if (response.field) {
					            setFieldError(response.field, response.message);
								$('#addUserForm').data("submitted", false);
					        }
					        else {
					            showToast(response.message, 'error');
					            $('#addUserModal').modal('hide');
					        }
			            }
		            },
		            error: function (xhr) {
						var errorMessage = (xhr.responseJSON && xhr.responseJSON.message) 
			                ? xhr.responseJSON.message 
			                : 'Hubo un error al agregar el usuario.';
			            var errorField = xhr.responseJSON && xhr.responseJSON.field 
			                ? xhr.responseJSON.field 
			                : null;
			            
						if (errorField) {
					        setFieldError(errorField, errorMessage);
							$('#addUserForm').data("submitted", false);
					    } else {
					        showToast(errorMessage, 'error');
					        $('#addUserModal').modal('hide');
					    }
		            },
					complete: function() {
	                	$("#addUserSpinner").addClass("d-none");
	                    $("#addUserIcon").removeClass("d-none");
	                    submitButton.prop('disabled', false);
	                }
		        });
			}
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
		
		// Email validation
		if (field.is('#addUserEmail')) {
	        const email = field.val();
	        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

	        if (!emailRegex.test(email)) {
	            errorMessage = 'Por favor ingrese un correo electrónico válido.';
	            isValid = false;
	        }
	    }
		
		// Username validation
	    if (field.is('#addUserUsername')) {
	        const username = field.val();
	        const usernameRegex = /^[a-zA-Z0-9_]+$/;

	        if (username.length < 5) {
	            errorMessage = 'El nombre de usuario debe tener al menos 5 caracteres.';
	            isValid = false;
	        } else if (!usernameRegex.test(username)) {
	            errorMessage = 'El nombre de usuario solo puede contener letras, números y guiones bajos.';
	            isValid = false;
	        }
	    }
		
		// First name validation
		if (field.is('#addUserFirstName')) {
		    const firstName = field.val();

		    if (firstName.length < 3) {
		        errorMessage = 'El nombre debe tener al menos 3 caracteres.';
		        isValid = false;
		    }
		}

		// Last name validation
		if (field.is('#addUserLastName')) {
		    const lastName = field.val();

		    if (lastName.length < 3) {
		        errorMessage = 'El apellido debe tener al menos 3 caracteres.';
		        isValid = false;
		    }
		}

		// Password validation
	    if (field.is('#addUserPassword')) {
	        const password = field.val();
	        const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

	        if (!passwordRegex.test(password)) {
				errorMessage = 'La contraseña debe tener 8 caracteres, una mayúscula, un número y un símbolo.';
	            isValid = false;
	        }
	    }

		// Confirm password validation
	    if (field.is('#addUserConfirmPassword')) {
	        const password = $('#addUserPassword').val();
	        const confirmPassword = field.val();

	        if (confirmPassword !== password) {
	            errorMessage = 'Las contraseñas no coinciden.';
	            isValid = false;
	        }
	    }

		// Profile photo validation
	    if (field.is('#addUserProfilePhoto')) {
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

$('#addUserProfilePhoto, #editUserProfilePhoto').on('change', function () {
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

function handleEditUserForm() {
	let isFirstSubmit = true;
	
	$('#editUserModal').on('hidden.bs.modal', function () {
        isFirstSubmit = true;
		$('#editUserForm').data("submitted", false);
    });
	
	$('#editUserForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
	        validateEditField($(this));
	    }
    });

    $('#editUserForm').on('submit', function(event) {
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

			var userId = $(this).data('userId');
            if (userId) {
                formData.append('userId', userId);
            }
			
			var submitButton = $(this).find('[type="submit"]');
			submitButton.prop('disabled', true);
			$("#editUserSpinner").removeClass("d-none");
			$("#editUserIcon").addClass("d-none");
			
	        if (cropper) {
	            cropper.getCroppedCanvas({
	                width: 460,
	                height: 460
	            }).toBlob(function (blob) {
	                formData.set('editUserProfilePhoto', blob, 'photo.png');
	                sendEditForm(formData);
	            }, 'image/png');
	        } else {
	            sendEditForm(formData);
	        }

	        function sendEditForm(formData) {
				formData.append('type', 'update');
				
	            $.ajax({
	                url: '/bookstudio/UserServlet',
	                type: 'POST',
	                data: formData,
	                dataType: 'json',
	                processData: false,
	                contentType: false,
	                success: function(response) {
	                    if (response.success) {
	                        updateRowInTable(response.data);
	                        $('#editUserModal').modal('hide');
	                        showToast('Usuario actualizado exitosamente.', 'success');
	                    } else {
							$('#editUserModal').modal('hide');
	                        showToast('Hubo un error al actualizar el usuario.', 'error');
	                    }
	                },
	                error: function() {
	                    $('#editUserModal').modal('hide');
	                    showToast('Hubo un error al actualizar el usuario.', 'error');
	                },
					complete: function() {
						$("#editUserSpinner").addClass("d-none");
						$("#editUserIcon").removeClass("d-none");
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
	
	// First name validation
	if (field.is('#editUserFirstName')) {
	    const firstName = field.val();

	    if (firstName.length < 3) {
	        errorMessage = 'El nombre debe tener al menos 3 caracteres.';
	        isValid = false;
	    }
	}

	// Last name validation
	if (field.is('#editUserLastName')) {
	    const lastName = field.val();

	    if (lastName.length < 3) {
	        errorMessage = 'El apellido debe tener al menos 3 caracteres.';
	        isValid = false;
	    }
	}

	// Password validation
    if (field.is('#editUserPassword')) {
        const password = field.val();
        const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

        if (!passwordRegex.test(password)) {
			errorMessage = 'La contraseña debe tener 8 caracteres, una mayúscula, un número y un símbolo.';
            isValid = false;
        }
    }
	
	// Profile photo validation
	if (field.is('#editUserProfilePhoto')) {
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
    $(document).on('click', '[data-bs-target="#addUserModal"]', function () {
		$('#addUserRole').selectpicker('destroy').empty().append(
            $('<option>', {
                value: 'administrador',
                text: 'Administrador'
            }),
            $('<option>', {
                value: 'bibliotecario',
                text: 'Bibliotecario'
            })
        );
		$('#addUserRole').selectpicker();
		
		$('#addUserForm')[0].reset();
		$('#addUserForm .is-invalid').removeClass('is-invalid');

	    $('#cropperContainerAdd').addClass('d-none');
		
		if (cropper) {
	        cropper.destroy();
	        cropper = null;
	    }
		
		$('#addUserForm .password-field').attr('type', 'password');
		$('#addUserForm .input-group-text').find('i').removeClass('bi-eye-slash').addClass('bi-eye');
    });

    // Details Modal
    $(document).on('click', '[data-bs-target="#detailsUserModal"]', function() {
        var userId = $(this).data('id');

        $.ajax({
            url: '/bookstudio/UserServlet',
            type: 'GET',
            data: { type: 'details', userId: userId },
            dataType: 'json',
            success: function(data) {
                $('#detailsUserID').text(data.userId);
                $('#detailsUserUsername').text(data.username);
                $('#detailsUserEmail').text(data.email);
				$('#detailsUserFirstName').text(data.firstName);				
				$('#detailsUserLastName').text(data.lastName);
				$('#detailsUserPassword').text(data.password);
				$('#detailsUserRole').text(data.role.charAt(0).toUpperCase() + data.role.slice(1));
				if (data.profilePhotoBase64) {
	                $('#detailsUserPhoto').attr('src', data.profilePhotoBase64);
	            } else {
	                $('#detailsUserPhoto').attr('src', '#');
	            }
            },
            error: function(status, error) {
                console.log("Error al cargar los detalles del usuario:", status, error);
            }
        });
    });
	
    // Edit Modal
    $(document).on('click', '[data-bs-target="#editUserModal"]', function() {
        var userId = $(this).data('id');

        $.ajax({
            url: '/bookstudio/UserServlet',
            type: 'GET',
            data: { type: 'details', userId: userId },
            dataType: 'json',
            success: function(data) {
				$('#editUserForm').data('userId', data.userId);
                $('#editUserUsername').val(data.username);
                $('#editUserEmail').val(data.email);
				$('#editUserFirstName').val(data.firstName);								
				$('#editUserLastName').val(data.lastName);
				$('#editUserPassword').val(data.password);
				
				$('#editUserRole').selectpicker('destroy').empty().append(
	                $('<option>', {
	                    value: 'administrador',
	                    text: 'Administrador'
	                }),
	                $('<option>', {
	                    value: 'bibliotecario',
	                    text: 'Bibliotecario'
	                })
	            );
                $('#editUserRole').val(data.role);
				$('#editUserRole').selectpicker();
				
				$('#editUserForm .is-invalid').removeClass('is-invalid');
				
				$('#editUserForm .password-field').attr('type', 'password');
				$('#editUserForm .input-group-text').find('i').removeClass('bi-eye-slash').addClass('bi-eye');
								
				placeholderColorEditSelect();
							
				$('#editUserForm').find('select').each(function() {
		            validateEditField($(this), true);
		        });
				
				$('#editUserProfilePhoto').val('');	
            },
            error: function(status, error) {
                console.log("Error al cargar los detalles del usuario para editar:", status, error);
            }
        });
		
		$('#cropperContainerEdit').addClass('d-none');
		
		if (cropper) {
	        cropper.destroy();
	        cropper = null;
	    }
    });
	
	// Delete Modal
	$('#deleteUserModal').on('show.bs.modal', function (event) {
	    var button = $(event.relatedTarget);
	    var userId = button.data('id');
	    $('#confirmDeleteUser').data('id', userId);

	    var isSubmitted = false;

	    $('#confirmDeleteUser').off('click').on('click', function () {
	        if (isSubmitted) return;
	        isSubmitted = true;

	        $('#confirmDeleteUserIcon').addClass('d-none');
	        $('#confirmDeleteUserSpinner').removeClass('d-none');
	        $('#confirmDeleteUser').prop('disabled', true);

	        var userId = $('#confirmDeleteUser').data('id');

	        $.ajax({
	            url: '/bookstudio/UserServlet',
	            type: 'POST',
	            data: {
	                type: 'delete',
	                userId: userId
	            },
	            dataType: 'json',
	            success: function (response) {
	                if (response.success) {
	                    var table = $('#userTable').DataTable();

	                    table.rows().every(function () {
	                        var data = this.data();
	                        if (data[0] == userId) {
	                            this.remove();
	                        }
	                    });

	                    table.draw();

	                    $('#deleteUserModal').modal('hide');
	                    showToast(response.message, 'success');
	                } else {
	                    $('#deleteUserModal').modal('hide');
	                    showToast(response.message, 'error');
	                }
	            },
	            error: function () {
	                $('#deleteUserModal').modal('hide');
	                showToast('Hubo un error al eliminar el usuario.', 'error');
	            },
	            complete: function () {
	                $('#confirmDeleteUserSpinner').addClass('d-none');
	                $('#confirmDeleteUserIcon').removeClass('d-none');
	                $('#confirmDeleteUser').prop('disabled', false);
	            }
	        });
	    });
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

$('#addUserProfilePhoto, #addUserProfilePhoto').on('change', function () {
    const file = this.files[0];

    if (file && file.type.startsWith('image/')) {
        let $container, $image;
        if ($(this).is('#addUserProfilePhoto')) {
            $container = $cropperContainerAdd;
            $image = $imageToCropAdd;
        } else {
            $container = $cropperContainerEdit;
            $image = $imageToCropEdit;
        }
        initializeCropper(file, $container, $image);
    } else {
        if ($(this).is('#addUserProfilePhoto')) {
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
    loadUsers();
    handleAddUserForm();
	handleEditUserForm();
    loadModalData();
    $('.selectpicker').selectpicker();
	setupBootstrapSelectDropdownStyles();
	placeholderColorSelect();
});