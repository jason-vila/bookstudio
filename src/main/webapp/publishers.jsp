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
		<jsp:param name="currentPage" value="publishers.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla Editoriales</h5>

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
						data-bs-toggle="modal" data-bs-target="#addPublisherModal"
						aria-label="Agregar editorial" disabled>
						<i class="bi bi-plus-lg me-2"></i> Agregar
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
					<table id="publisherTable" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">Nombre</th>
								<th scope="col" class="text-start">Nacionalidad</th>
								<th scope="col" class="text-start">Género Literario</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center">Foto</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyPublishers">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Publisher Modal -->
	<div class="modal fade" id="addPublisherModal" tabindex="-1" aria-labelledby="addPublisherModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="addPublisherModalLabel">Agregar Editorial</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addPublisherForm" enctype="multipart/form-data" novalidate>
	                    <!-- Publisher Information Section -->
	                    <div class="row">
	                        <!-- Publisher Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherName" class="form-label">Nombre <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addPublisherName" 
	                                name="addPublisherName" 
	                                placeholder="Ingrese el nombre de la editorial" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Nationality Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherNationality" class="form-label">Nacionalidad <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addPublisherNationality" 
	                                name="addPublisherNationality" 
	                                placeholder="Ingrese la nacionalidad de la editorial" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Genre and Foundation Year Section -->
	                    <div class="row">
	                        <!-- Literary Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLiteraryGenre" class="form-label">Género Literario <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addLiteraryGenre" 
	                                name="addLiteraryGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un género literario" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Foundation Year Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addFoundationYear" class="form-label">Año de Fundación <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="addFoundationYear" 
	                                name="addFoundationYear" 
	                                min="1000" 
	                                placeholder="Ingrese el año de fundación" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Website and Address Section -->
	                    <div class="row">
	                        <!-- Website Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherWebsite" class="form-label">Página Web</label>
	                            <input 
	                                type="url" 
	                                class="form-control" 
	                                id="addPublisherWebsite" 
	                                name="addPublisherWebsite" 
	                                placeholder="https://ejemplo.com"
	                            >
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherAddress" class="form-label">Dirección</label>
	                            <textarea 
	                                class="form-control" 
	                                id="addPublisherAddress" 
	                                name="addPublisherAddress" 
	                                rows="1" 
	                                placeholder="Ingrese la dirección de la editorial"
	                            ></textarea>
	                        </div>
	                    </div>
	                    
	                    <!-- Status and Photo Section -->
	                    <div class="row">
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addPublisherStatus" 
	                                name="addPublisherStatus" 
	                                title="Seleccione un estado" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Photo Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisherPhoto" class="form-label">Foto</label>
	                            <input 
	                                type="file" 
	                                class="form-control" 
	                                id="addPublisherPhoto" 
	                                name="addPublisherPhoto" 
	                                accept="image/*"
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Image Cropper Container -->
	                    <div id="cropperContainerAdd" class="d-none">
	                        <img id="imageToCropAdd" src="#" alt="Imagen" />
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Add Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addPublisherForm" id="addPublisherBtn">
	                    <span id="addPublisherIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
	                    <span id="addPublisherSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Agregar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Publisher Details Modal -->
	<div class="modal fade" id="detailsPublisherModal" tabindex="-1" aria-labelledby="detailsPublisherModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsPublisherModalLabel">Detalles de la Editorial</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <!-- ID and Name Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">ID</h6>
	                        <p class="fw-bold" id="detailsPublisherID"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Nombre</h6>
	                        <p class="fw-bold" id="detailsPublisherName"></p>
	                    </div>
	                </div>
	                
	                <!-- Nationality and Genre Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Nacionalidad</h6>
	                        <p class="fw-bold" id="detailsPublisherNationality"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Género Literario</h6>
	                        <p class="fw-bold" id="detailsPublisherGenre"></p>
	                    </div>
	                </div>
	                
	                <!-- Foundation Year and Website Section -->
	                <div class="row">  
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Año de Fundación</h6>
	                        <p class="fw-bold" id="detailsPublisherYear"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Página Web</h6>
	                        <p class="fw-bold" id="detailsPublisherWebsite"><a href="" target="_blank"></a></p>
	                    </div>
	                </div>
	                
	                <!-- Address and Status Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Dirección</h6>
	                        <p class="fw-bold" id="detailsPublisherAddress"></p>
	                    </div>
	                    
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Estado</h6>
	                        <p id="detailsPublisherStatus"></p>
	                    </div>
	                </div>
	                
	                <!-- Photo Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Foto</h6>
	                        <img src="" id="detailsPublisherPhoto" alt="Foto" class="img-fluid" style="width: 100px; height: 100px;">
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
	
	<!-- Edit Publisher Modal -->
	<div class="modal fade" id="editPublisherModal" tabindex="-1" aria-labelledby="editPublisherModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editPublisherModalLabel">Editar Editorial</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="editPublisherForm" enctype="multipart/form-data" novalidate>
	                    <!-- Publisher Information Section -->
	                    <div class="row">
	                        <!-- Publisher Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherName" class="form-label">Nombre <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editPublisherName" 
	                                name="editPublisherName" 
	                                placeholder="Ingrese el nombre de la editorial" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Nationality Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherNationality" class="form-label">Nacionalidad <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editPublisherNationality" 
	                                name="editPublisherNationality" 
	                                placeholder="Ingrese la nacionalidad de la editorial" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Genre and Foundation Year Section -->
	                    <div class="row">
	                        <!-- Literary Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLiteraryGenre" class="form-label">Género Literario <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editLiteraryGenre" 
	                                name="editLiteraryGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Foundation Year Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editFoundationYear" class="form-label">Año de Fundación <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="editFoundationYear" 
	                                name="editFoundationYear" 
	                                min="1000" 
	                                placeholder="Ingrese el año de fundación" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Website and Address Section -->
	                    <div class="row">
	                        <!-- Website Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherWebsite" class="form-label">Página Web</label>
	                            <input 
	                                type="url" 
	                                class="form-control" 
	                                id="editPublisherWebsite" 
	                                name="editPublisherWebsite" 
	                                placeholder="https://ejemplo.com" 
	                                required
	                            >
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherAddress" class="form-label">Dirección</label>
	                            <textarea 
	                                class="form-control" 
	                                id="editPublisherAddress" 
	                                name="editPublisherAddress" 
	                                rows="1" 
	                                placeholder="Ingrese la dirección de la editorial"
	                            ></textarea>
	                        </div>
	                    </div>
	                    
	                    <!-- Status and Photo Section -->
	                    <div class="row">
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editPublisherStatus" 
	                                name="editPublisherStatus" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Photo Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisherPhoto" class="form-label">Foto</label>
	                            <input 
	                                type="file" 
	                                class="form-control" 
	                                id="editPublisherPhoto" 
	                                name="editPublisherPhoto" 
	                                accept="image/*"
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Image Cropper Container -->
	                    <div id="cropperContainerEdit" class="d-none">
	                        <img id="imageToCropEdit" src="#" alt="Imagen" />
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Update Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editPublisherForm" id="editPublisherBtn">
	                    <span id="editPublisherIcon" class="me-2"><i class="bi bi-floppy"></i></span>
	                    <span id="editPublisherSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
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
		<jsp:param name="currentPage" value="publishers.js" />
	</jsp:include>
</body>
</html>