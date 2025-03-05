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
		<jsp:param name="currentPage" value="students.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla Estudiantes</h5>

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
						data-bs-toggle="modal" data-bs-target="#addStudentModal"
						aria-label="Agregar estudiante" disabled>
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
					<table id=studentTable class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">DNI</th>
								<th scope="col" class="text-start">Nombres</th>
								<th scope="col" class="text-start">Apellidos</th>
								<th scope="col" class="text-start">Teléfono</th>
								<th scope="col" class="text-start">Correo electrónico</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyStudents">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Student Modal -->
	<div class="modal fade" id="addStudentModal" tabindex="-1" aria-labelledby="addStudentModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="addStudentModalLabel">Agregar Estudiante</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addStudentForm" accept-charset="UTF-8" novalidate>
	                    <!-- Personal Information Section -->
	                    <div class="row">
	                        <!-- DNI Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentDNI" class="form-label">DNI <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="addStudentDNI" 
	                                name="addStudentDNI" 
	                                maxlength="8" 
	                                pattern="\d{8}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 8);" 
	                                placeholder="Ingrese el DNI del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- First Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentFirstName" class="form-label">Nombres <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addStudentFirstName" 
	                                name="addStudentFirstName" 
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los nombres del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Last Name and Address Section -->
	                    <div class="row">
	                        <!-- Last Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentLastName" class="form-label">Apellidos <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addStudentLastName" 
	                                name="addStudentLastName" 
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los apellidos del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentAddress" class="form-label">Dirección <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addStudentAddress" 
	                                name="addStudentAddress" 
	                                placeholder="Ingrese la dirección del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Phone and Email Section -->
	                    <div class="row">
	                        <!-- Phone Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentPhone" class="form-label">Teléfono <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="addStudentPhone" 
	                                name="addStudentPhone" 
	                                maxlength="9" 
	                                pattern="\d{9}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 9);" 
	                                placeholder="Ingrese el teléfono del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Email Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentEmail" class="form-label">Correo Electrónico <span class="text-danger">*</span></label>
	                            <input 
	                                type="email" 
	                                class="form-control" 
	                                id="addStudentEmail" 
	                                name="addStudentEmail" 
	                                placeholder="ejemplo@correo.com" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Birth Date and Gender Section -->
	                    <div class="row">
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentBirthDate" class="form-label">Fecha de Nacimiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addStudentBirthDate" 
	                                name="addStudentBirthDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Gender Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentGender" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addStudentGender" 
	                                name="addStudentGender" 
	                                title="Seleccione un género" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Faculty and Status Section -->
	                    <div class="row">
	                        <!-- Faculty Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentFaculty" class="form-label">Facultad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addStudentFaculty" 
	                                name="addStudentFaculty" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione una facultad" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStudentStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addStudentStatus" 
	                                name="addStudentStatus" 
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
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addStudentForm" id="addStudentBtn">
	                    <span id="addStudentIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
	                    <span id="addStudentSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Agregar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Student Details Modal -->
	<div class="modal fade" id="detailsStudentModal" tabindex="-1" aria-labelledby="detailsStudentModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsStudentModalLabel">Detalles del Estudiante</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <!-- ID and DNI Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">ID</h6>
	                        <p class="fw-bold" id="detailsStudentID"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">DNI</h6>
	                        <p class="fw-bold" id="detailsStudentDNI"></p>
	                    </div>
	                </div>
	                
	                <!-- First Name and Last Name Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Nombres</h6>
	                        <p class="fw-bold" id="detailsStudentFirstName"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Apellidos</h6>
	                        <p class="fw-bold" id="detailsStudentLastName"></p>
	                    </div>
	                </div>
	                
	                <!-- Address and Phone Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Dirección</h6>
	                        <p class="fw-bold" id="detailsStudentAddress"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Teléfono</h6>
	                        <p class="fw-bold" id="detailsStudentPhone"></p>
	                    </div>
	                </div>
	                
	                <!-- Email and Birth Date Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Correo Electrónico</h6>
	                        <p class="fw-bold" id="detailsStudentEmail"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Fecha de Nacimiento</h6>
	                        <p class="fw-bold" id="detailsStudentBirthDate"></p>
	                    </div>
	                </div>
	                
	                <!-- Gender and Faculty Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Género</h6>
	                        <p class="fw-bold" id="detailsStudentGender"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Facultad</h6>
	                        <p class="fw-bold" id="detailsStudentFaculty"></p>
	                    </div>
	                </div>
	                
	                <!-- Status Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Estado</h6>
	                        <p id="detailsStudentStatus"></p>
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
	
	<!-- Edit Student Modal -->
	<div class="modal fade" id="editStudentModal" tabindex="-1" aria-labelledby="editStudentModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editStudentModalLabel">Editar Estudiante</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="editStudentForm" accept-charset="UTF-8" novalidate>
	                    <!-- Personal Information Section -->
	                    <div class="row">
	                        <!-- DNI Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentDNI" class="form-label">DNI</label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="editStudentDNI" 
	                                disabled
	                            >
	                        </div>
	                        
	                        <!-- First Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentFirstName" class="form-label">Nombres</label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editStudentFirstName" 
	                                disabled
	                            >
	                        </div>
	                    </div>
	                    
	                    <!-- Last Name and Address Section -->
	                    <div class="row">
	                        <!-- Last Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentLastName" class="form-label">Apellidos</label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editStudentLastName" 
	                                name="editStudentLastName" 
	                                disabled
	                            >
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentAddress" class="form-label">Dirección <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editStudentAddress" 
	                                name="editStudentAddress" 
	                                placeholder="Ingrese la dirección del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Phone and Email Section -->
	                    <div class="row">
	                        <!-- Phone Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentPhone" class="form-label">Teléfono <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="editStudentPhone" 
	                                name="editStudentPhone" 
	                                maxlength="9" 
	                                pattern="\d{9}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 9);" 
	                                placeholder="Ingrese el teléfono del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Email Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentEmail" class="form-label">Correo Electrónico <span class="text-danger">*</span></label>
	                            <input 
	                                type="email" 
	                                class="form-control" 
	                                id="editStudentEmail" 
	                                name="editStudentEmail" 
	                                placeholder="ejemplo@correo.com" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Birth Date and Gender Section -->
	                    <div class="row">
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentBirthDate" class="form-label">Fecha de Nacimiento</label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editStudentBirthDate" 
	                                disabled
	                            >
	                        </div>
	                        
	                        <!-- Gender Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentGender" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editStudentGender" 
	                                name="editStudentGender" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Faculty and Status Section -->
	                    <div class="row">
	                        <!-- Faculty Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStudentFaculty" class="form-label">Facultad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editStudentFaculty" 
	                                name="editStudentFaculty" 
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
	                            <label for="editStudentStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editStudentStatus" 
	                                name="editStudentStatus" 
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
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editStudentForm" id="editStudentBtn">
	                    <span id="editStudentIcon" class="me-2"><i class="bi bi-floppy"></i></span>
	                    <span id="editStudentSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
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
		<jsp:param name="currentPage" value="students.js" />
	</jsp:include>
</body>
</html>