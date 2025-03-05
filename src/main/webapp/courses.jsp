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
		<jsp:param name="currentPage" value="courses.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla Cursos</h5>

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
						data-bs-toggle="modal" data-bs-target="#addCourseModal"
						aria-label="Agregar curso" disabled>
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
				<div id="tableContainer" class="d-none small">
					<table id="courseTable" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">Nombre</th>
								<th scope="col" class="text-start">Nivel</th>
								<th scope="col" class="text-start">Descripción</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyCourses">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</div>
			</div>
		</section>
	</main>

	<!-- Add Course Modal -->
	<div class="modal fade" id="addCourseModal" tabindex="-1" aria-labelledby="addCourseModalLabel" aria-hidden="true" data-bs-backdrop="static">
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		        <!-- Modal Header -->
		        <header class="modal-header">
		            <h5 class="modal-title text-body-emphasis" id="addCourseModalLabel">Agregar Curso</h5>
		            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		        </header>
		        
		        <!-- Modal Body -->
		        <div class="modal-body">
		            <form id="addCourseForm" accept-charset="UTF-8" novalidate>
		                <div class="row">
		                    <!-- Course Name Field -->
		                    <div class="col-md-6 mb-3">
		                        <label for="addCourseName" class="form-label">Nombre <span class="text-danger">*</span></label>
		                        <input 
		                            type="text" 
		                            class="form-control" 
		                            id="addCourseName" 
		                            name="addCourseName" 
		                            placeholder="Ingrese el nombre del curso" 
		                            required
		                        >
		                        <div class="invalid-feedback"></div>
		                    </div>
		                    
		                    <!-- Course Level Field -->
		                    <div class="col-md-6 mb-3">
		                        <label for="addCourseLevel" class="form-label">Nivel <span class="text-danger">*</span></label>
		                        <select 
		                            class="selectpicker form-control placeholder-color" 
		                            id="addCourseLevel" 
		                            name="addCourseLevel" 
		                            title="Seleccione un nivel" 
		                            required
		                        >
		                            <!-- Options will be dynamically populated via JavaScript -->
		                        </select>
		                        <div class="invalid-feedback"></div>
		                    </div>
		                </div>
		                
		                <div class="row">
		                    <!-- Course Description Field -->
		                    <div class="col-md-6 mb-3">
		                        <label for="addCourseDescription" class="form-label">Descripción</label>
		                        <textarea 
		                            class="form-control" 
		                            id="addCourseDescription" 
		                            name="addCourseDescription" 
		                            rows="4" 
		                            placeholder="Ingrese una breve descripción"
		                        ></textarea>
		                    </div>
		                    
		                    <!-- Course Status Field -->
		                    <div class="col-md-6 mb-3">
		                        <label for="addCourseStatus" class="form-label">Estado <span class="text-danger">*</span></label>
		                        <select 
		                            class="selectpicker form-control placeholder-color" 
		                            id="addCourseStatus" 
		                            name="addCourseStatus" 
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
		            <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addCourseForm" id="addCourseBtn">
		                <span id="addCourseIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
		                <span id="addCourseSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
		                Agregar
		            </button>
		        </footer>
		    </div>
		</div>
	</div>
	
	<!-- Details Course Modal -->
	<div class="modal fade" id="detailsCourseModal" tabindex="-1" aria-labelledby="detailsCourseModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsCourseModalLabel">Detalles del Curso</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <!-- ID and Name Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">ID</h6>
	                        <p class="fw-bold" id="detailsCourseID"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Nombre</h6>
	                        <p class="fw-bold" id="detailsCourseName"></p>
	                    </div>
	                </div>
	                
	                <!-- Level and Status Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Nivel</h6>
	                        <p class="fw-bold" id="detailsCourseLevel"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Estado</h6>
	                        <p id="detailsCourseStatus"></p>
	                    </div>
	                </div>
	                
	                <!-- Description Section -->
	                <div class="row">
	                    <div class="col-md-12 mb-3">
	                        <h6 class="small text-muted">Descripción</h6>
	                        <p class="fw-bold" id="detailsCourseDescription"></p>
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
	
	<!-- Edit Course Modal -->
	<div class="modal fade" id="editCourseModal" tabindex="-1" aria-labelledby="editCourseModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editCourseModalLabel">Editar Curso</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="editCourseForm" accept-charset="UTF-8" novalidate>
	                    <!-- Course Name and Level Section -->
	                    <div class="row">
	                        <!-- Course Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editCourseName" class="form-label">Nombre <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editCourseName" 
	                                name="editCourseName" 
	                                placeholder="Ingrese el nombre del curso" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Course Level Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editCourseLevel" class="form-label">Nivel <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editCourseLevel" 
	                                name="editCourseLevel" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Course Description and Status Section -->
	                    <div class="row">
	                        <!-- Course Description Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editCourseDescription" class="form-label">Descripción</label>
	                            <textarea 
	                                class="form-control" 
	                                id="editCourseDescription" 
	                                name="editCourseDescription" 
	                                rows="4" 
	                                placeholder="Ingrese una breve descripcion"
	                            ></textarea>
	                        </div>
	                        
	                        <!-- Course Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editCourseStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editCourseStatus" 
	                                name="editCourseStatus" 
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
	                
	                <!-- Update Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editCourseForm" id="editCourseBtn">
	                    <span id="editCourseIcon" class="me-2"><i class="bi bi-floppy"></i></span>
	                    <span id="editCourseSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
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
		<jsp:param name="currentPage" value="courses.js" />
	</jsp:include>
</body>
</html>