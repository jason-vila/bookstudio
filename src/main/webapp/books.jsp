<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="auto">
<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:include page="WEB-INF/includes/styles.jsp"></jsp:include>
    <title>BookStudio</title>
    <link href="images/logo-dark.png" rel="icon" media="(prefers-color-scheme: light)">
    <link href="images/logo-light.png" rel="icon" media="(prefers-color-scheme: dark)">
</head>
<body>
	<!-- Set user role from session -->
	<c:set var="userRole" value="${sessionScope.role}" />

	<!-- ===================== Header ===================== -->
	<jsp:include page="WEB-INF/includes/header.jsp"></jsp:include>

	<!-- ===================== Sidebar ==================== -->
	<jsp:include page="WEB-INF/includes/sidebar.jsp">
		<jsp:param name="currentPage" value="books.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla Libros</h5>

				<!-- Excel Button -->
				<button
					class="btn btn-custom-secondary excel d-flex align-items-center me-2"
					id="generateExcel" aria-label="Generar Excel" disabled>
					<i class="bi bi-file-earmark-excel text-success me-2"></i>
					Excel
				</button>

				<!-- PDF Button -->
				<button
					class="btn btn-custom-secondary d-flex align-items-center me-2"
					id="generatePDF" aria-label="Generar PDF" disabled>
					<i class="bi bi-file-earmark-pdf text-danger me-2"></i>
					PDF
				</button>

				<c:if test="${userRole == 'administrador'}">
					<!-- Add Button -->
					<button class="btn btn-custom-primary d-flex align-items-center"
						data-bs-toggle="modal" data-bs-target="#addBookModal"
						aria-label="Agregar libro" disabled>
						<i class="bi bi-plus-lg me-2"></i>
						Agregar
					</button>
				</c:if>
			</header>

			<!-- Card Body -->
			<div class="card-body">
				<!-- Loading Spinner -->
				<div id="spinnerLoad"
					class="d-flex justify-content-center align-items-center h-100">
					<div class="spinner-border" role="status">
						<span class="visually-hidden">Cargando...</span>
					</div>
				</div>

				<!-- Table Container -->
				<section id="tableContainer" class="d-none small">
					<table id="bookTable" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">Título</th>
								<th scope="col" class="text-center">Ejemplares Disponibles</th>
								<th scope="col" class="text-center">Ejemplares Prestados</th>
								<th scope="col" class="text-start">Autor</th>
								<th scope="col" class="text-start">Editorial</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyBooks">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Book Modal -->
	<div class="modal fade" id="addBookModal" tabindex="-1" aria-labelledby="addBookModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="addBookModalLabel">Agregar Libro</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addBookForm" accept-charset="UTF-8" novalidate>
	                    <!-- Book Title and Total Copies Section -->
	                    <div class="row">
	                        <!-- Book Title Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookTitle" class="form-label">Título <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addBookTitle" 
	                                name="addBookTitle" 
	                                placeholder="Ingrese el título del libro" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Total Copies Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookTotalCopies" class="form-label">Ejemplares Totales <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="addBookTotalCopies" 
	                                name="addBookTotalCopies" 
	                                min="1" 
	                                max="1000" 
	                                placeholder="Ingrese la cantidad de ejemplares" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Author and Publisher Section -->
	                    <div class="row">
	                        <!-- Author Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookAuthor" class="form-label">Autor <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addBookAuthor" 
	                                name="addBookAuthor" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un autor" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Publisher Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookPublisher" class="form-label">Editorial <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addBookPublisher" 
	                                name="addBookPublisher" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione una editorial" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Course and Release Date Section -->
	                    <div class="row">
	                        <!-- Course Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookCourse" class="form-label">Curso <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addBookCourse" 
	                                name="addBookCourse" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un curso" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Release Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addReleaseDate" class="form-label">Fecha de lanzamiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addReleaseDate" 
	                                name="addReleaseDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>   
	                    </div>
	                    
	                    <!-- Genre and Status Section -->
	                    <div class="row">
	                        <!-- Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookGenre" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addBookGenre" 
	                                name="addBookGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un género" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBookStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addBookStatus" 
	                                name="addBookStatus" 
	                                title="Seleccione un estado" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Add Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addBookForm" id="addBookBtn">
	                    <span id="addBookIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
	                    <span id="addBookSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Agregar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Book Details Modal -->
	<div class="modal fade" id="detailsBookModal" tabindex="-1" aria-labelledby="detailsBookModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	        	<!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsBookModalLabel">Detalles del Libro</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	            	<!-- ID and Title Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">ID</h6>
	                        <p class="fw-bold" id="detailsBookID"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Título</h6>
	                        <p class="fw-bold" id="detailsBookTitle"></p>
	                    </div>
	                </div>
	                
	                <!-- Avaible and Loaned Copies Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Ejemplares Disponibles</h6>
	                        <p class="fw-bold" id="detailsBookAvaibleCopies"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Ejemplares Prestados</h6>
	                        <p class="fw-bold" id="detailsBookLoanedCopies"></p>
	                    </div>
	                </div>
	                
	                <!-- Author and Publisher Section -->
	                <div class="row">
	                	<div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Autor</h6>
	                        <p class="fw-bold" id="detailsBookAuthor"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Editorial</h6>
	                        <p class="fw-bold" id="detailsBookPublisher"></p>
	                    </div>
	                </div>
	                
	                <!-- Course and Release Date Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Curso</h6>
	                        <p class="fw-bold" id="detailsBookCourse"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Fecha de lanzamiento</h6>
	                        <p class="fw-bold" id="detailsReleaseDate"></p>
	                    </div>
	                </div>
	                
	                <!-- Genre and Status Section -->
	                <div class="row">   
	                    <div class="col-md-6">
	                        <h6 class="small text-muted">Género</h6>
	                        <p class="fw-bold" id="detailsBookGenre"></p>
	                    </div>
	                    
	                    <div class="col-md-6">
	                        <h6 class="small text-muted">Estado</h6>
	                        <p id="detailsBookStatus"></p>
	                    </div>
	                </div>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Close Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cerrar</button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Edit Book Modal -->
	<div class="modal fade" id="editBookModal" tabindex="-1" aria-labelledby="editBookModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editBookModalLabel">Editar Libro</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="editBookForm" accept-charset="UTF-8" novalidate>
	                    <!-- Title and Total Copies Section -->
	                    <div class="row">
	                        <!-- Book Title Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookTitle" class="form-label">Título <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editBookTitle" 
	                                name="editBookTitle" 
	                                placeholder="Ingrese el título del libro" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Total Copies Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookTotalCopies" class="form-label">Ejemplares Totales <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="editBookTotalCopies" 
	                                name="editBookTotalCopies" 
	                                min="1" 
	                                max="1000" 
	                                placeholder="Ingrese la cantidad de ejemplares" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Author and Publisher Section -->
	                    <div class="row">
	                        <!-- Author Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookAuthor" class="form-label">Autor <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editBookAuthor" 
	                                name="editBookAuthor" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Publisher Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookPublisher" class="form-label">Editorial <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editBookPublisher" 
	                                name="editBookPublisher" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Course and Release Date Section -->
	                    <div class="row">
	                        <!-- Course Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookCourse" class="form-label">Curso <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editBookCourse" 
	                                name="editBookCourse" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Release Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editReleaseDate" class="form-label">Fecha de lanzamiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editReleaseDate" 
	                                name="editReleaseDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div> 
	                    </div>
	                    
	                    <!-- Genre and Status Section -->
	                    <div class="row">
	                        <!-- Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookGenre" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editBookGenre" 
	                                name="editBookGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBookStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editBookStatus" 
	                                name="editBookStatus" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Update Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editBookForm" id="editBookBtn">
	                    <span id="editBookIcon" class="me-2"><i class="bi bi-floppy"></i></span>
	                    <span id="editBookSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Actualizar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>

	<!-- Toast Container -->
	<div class="toast-container" id="toast-container">
		<!-- Toasts will be added here by JavaScript -->
	</div>

	<jsp:include page="WEB-INF/includes/scripts.jsp">
		<jsp:param name="currentPage" value="books.js" />
	</jsp:include>
</body>
</html>