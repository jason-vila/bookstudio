/**
 * login.js
 * Handles the login form validation, submission, and background image update based on the theme.
 * Implements real-time validation for username and password fields, and authenticates using
 * an AJAX request. Also initializes and updates particle animation based on the current theme.
 * 
 * @author [Jason]
 */

$(document).ready(function() {
	let formSubmitted = false;

	// Validation functions
	function validateUsername() {
		const username = $("#txtUsername").val().trim();
		if (username === "" || username.length < 3) {
			if (formSubmitted) $("#txtUsername").addClass("is-invalid");
			return false;
		} else {
			$("#txtUsername").removeClass("is-invalid");
			return true;
		}
	}

	function validatePassword() {
		const password = $("#txtPassword").val().trim();
		if (password === "" || password.length < 6) {
			if (formSubmitted) $("#txtPassword").addClass("is-invalid");
			return false;
		} else {
			$("#txtPassword").removeClass("is-invalid");
			return true;
		}
	}

	// Event listeners
	$("#txtUsername, #txtPassword").on("input", function() {
		if (formSubmitted) {
			validateUsername();
			validatePassword();
		}
	});

	// Form submission handling
	$("#loginForm").on("submit", function(event) {
		event.preventDefault();
		formSubmitted = true;

		const isUsernameValid = validateUsername();
		const isPasswordValid = validatePassword();

		if (!isUsernameValid || !isPasswordValid) return;

		$("#loginBtn").prop("disabled", true);
		$("#spinner").removeClass("d-none");
		$("#loginText").addClass("d-none");

		const formData = {
			type: "login",
			txtUsername: $("#txtUsername").val().trim(),
			txtPassword: $("#txtPassword").val().trim()
		};

		$.ajax({
			type: "POST",
			url: "LoginServlet",
			data: formData,
			dataType: "json",
			success: function(response) {
				if (response.success) {
					window.location.href = "dashboard.jsp";
				} else {
					showToast(response.message, "error");
					$("#txtUsername").removeClass("is-invalid");
					$("#txtPassword").removeClass("is-invalid");
				}
			},
			error: function() {
				showToast("Se produjo un error inesperado.", "error");
				$("#txtUsername").removeClass("is-invalid");
				$("#txtPassword").removeClass("is-invalid");
			},
			complete: function() {
				$("#loginBtn").prop("disabled", false);
				$("#spinner").addClass("d-none");
				$("#loginText").removeClass("d-none");
			}
		});
	});
});