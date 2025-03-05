<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="auto">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="utils/color-modes.js"></script>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
	<link rel="stylesheet" href="css/styles-login.css">
	<title>BookStudio</title>
	<link href="images/logo-dark.png" rel="icon" media="(prefers-color-scheme: light)">
	<link href="images/logo-light.png" rel="icon" media="(prefers-color-scheme: dark)">
</head>
<body>
	<!-- Background -->
	<div class="position-fixed w-100 h-100 bg-body"></div>
	<!-- Centered container -->
	<div class="d-flex justify-content-center align-items-center vh-100">
		<!-- ===================== Main Content ==================== -->
		<main class="w-100 mx-4" style="max-width: 384px;">
			<!-- Card Container -->
			<section class="card border">
				<!-- Card header -->
				<header class="card-header text-center bg-body-secondary mt-2 mb-2">
					<h4 class="fw-bold mb-0 text-start">Iniciar Sesión</h4>
					<p class="text-muted mb-0 text-start fs-6">Empieza a gestionar tus libros.</p>
				</header>
				
				<!-- Card body -->
				<div class="card-body">
					<form id="loginForm" novalidate>
						<!-- Username field -->
						<div class="mb-4">
							<label for="txtUsername" class="form-label">Nombre de Usuario</label>
							<input
	                            id="txtUsername" 
	                            name="txtUsername" 
	                            type="text" 
	                            class="form-control" 
	                            placeholder="Ingrese su nombre de usuario" 
	                            aria-describedby="usernameHelp" 
	                            autocomplete="username">
						</div>
						
						<!-- Password field -->
						<div class="mb-4">
							<div class="d-flex justify-content-between align-items-center">
								<label for="txtPassword" class="form-label">Contraseña</label>
								<a href="#" class="text-body-emphasis"><small>¿Olvidaste tu contraseña?</small></a>
							</div>
							<div class="input-group">
								<input
	                                id="txtPassword" 
	                                name="txtPassword" 
	                                type="password" 
	                                class="form-control password-field" 
	                                data-toggle-id="1" 
	                                placeholder="Ingrese su contraseña" 
	                                aria-describedby="passwordHelp" 
	                                autocomplete="current-password">
	                            <span class="input-group-text eye-icon" data-toggle-id="1">
	                                <i class="bi bi-eye"></i>
	                            </span>
							</div>
						</div>
						
						<!-- Login Button -->
						<button type="submit" class="btn btn-custom-primary w-100" id="loginBtn">
		                    <span id="spinner" class="spinner-border spinner-border-sm d-none" role="status" aria-hidden="true"></span>
		                    <span id="loginText">Iniciar Sesión</span>
		                </button>

						<!-- Google Button -->
						<button type="button" class="btn btn-custom-secondary w-100 mt-3 d-flex align-items-center position-relative">
							<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="1.25em" height="1.25em" viewBox="0 0 48 48" class="LgbsSe-Bz112c">
								<g>
									<path fill="#EA4335" d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"></path>
									<path fill="#4285F4" d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"></path>
									<path fill="#FBBC05" d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"></path>
									<path fill="#34A853" d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"></path>
									<path fill="none" d="M0 0h48v48H0z"></path>
								</g>
							</svg>
							<span class="w-100 text-center">Continuar con Google</span>
						</button>
					</form>
				</div>
			</section>
		</main>
	</div>

	<!-- Toast Container -->
	<div class="toast-container" id="toast-container"></div>

	<!-- Theme toggle button -->
	<div class="dropdown position-fixed bottom-0 end-0 mb-3 me-3">
		<jsp:include page="WEB-INF/includes/buttonTheme.jsp">
			<jsp:param name="dropdownDirection" value="2" />
		</jsp:include>
	</div>

	<!-- External Libraries -->
	<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>

	<!-- Custom Scripts -->
	<script src="js/login.js"></script>
	<script src="utils/toast.js"></script>
	<script src="utils/password.js"></script>
</body>
</html>