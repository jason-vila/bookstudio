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
		<jsp:param name="currentPage" value="loans.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla Préstamos</h5>

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
						data-bs-toggle="modal" data-bs-target="#addLoanModal"
						aria-label="Prestar libro" disabled>
						<i class="bi bi-plus-lg me-2"></i>
						Prestar
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
					<table id="loanTable" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">Libro</th>
								<th scope="col" class="text-start">Estudiante</th>
								<th scope="col" class="text-center">Fecha Préstamo</th>
								<th scope="col" class="text-center">Fecha Devolución</th>
								<th scope="col" class="text-center">Cantidad</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyLoans">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Loan Modal -->
	<div class="modal fade" id="addLoanModal" tabindex="-1" aria-labelledby="addLoanModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="addLoanModalLabel">Agregar Préstamo</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addLoanForm" accept-charset="UTF-8" novalidate>
	                    <!-- Book and Student Section -->
	                    <div class="row">
	                        <!-- Book Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLoanBook" class="form-label">Libro <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addLoanBook" 
	                                name="addLoanBook" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un libro" 
	                                required
	                            >
	                                <!-- Options will be populated dynamically via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Student Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLoanStudent" class="form-label">Estudiante <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addLoanStudent" 
	                                name="addLoanStudent" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un estudiante" 
	                                required
	                            >
	                                <!-- Options will be populated dynamically via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Loan Dates Section -->
	                    <div class="row">
	                        <!-- Loan Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLoanDate" class="form-label">Fecha Préstamo</label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addLoanDate" 
	                                disabled
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Return Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addReturnDate" class="form-label">Fecha Devolución <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addReturnDate" 
	                                name="addReturnDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Quantity and Observation Section -->
	                    <div class="row">
	                        <!-- Quantity Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLoanQuantity" class="form-label">Cantidad <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="addLoanQuantity" 
	                                name="addLoanQuantity" 
	                                min="1" 
	                                placeholder="Ingrese la cantidad" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Observation Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLoanObservation" class="form-label">Observación</label>
	                            <textarea 
	                                class="form-control" 
	                                id="addLoanObservation" 
	                                name="addLoanObservation" 
	                                rows="1" 
	                                placeholder="Ingrese cualquier observación opcional"
	                            ></textarea>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Loan Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addLoanForm" id="addLoanBtn">
	                    <span id="addLoanIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
	                    <span id="addLoanSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Prestar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Loan Details Modal -->
	<div class="modal fade" id="detailsLoanModal" tabindex="-1" aria-labelledby="detailsLoanModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsLoanModalLabel">Detalles del Préstamo</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <!-- ID and Book Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">ID</h6>
	                        <p class="fw-bold" id="detailsLoanID"></p>
	                    </div>
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Libro</h6>
	                        <p class="fw-bold" id="detailsLoanBook"></p>
	                    </div>
	                </div>
	                
	                <!-- Student and Loan Date Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Estudiante</h6>
	                        <p class="fw-bold" id="detailsLoanStudent"></p>
	                    </div>
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Fecha de Préstamo</h6>
	                        <p class="fw-bold" id="detailsLoanDate"></p>
	                    </div>
	                </div>
	                
	                <!-- Return Date and Quantity Section -->
	                <div class="row">
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Fecha de Devolución</h6>
	                        <p class="fw-bold" id="detailsReturnDate"></p>
	                    </div>
	                    <div class="col-md-6 mb-3">
	                        <h6 class="small text-muted">Cantidad</h6>
	                        <p class="fw-bold" id="detailsLoanQuantity"></p>
	                    </div>
	                </div>
	                
	                <!-- Status and Observation Section -->
	                <div class="row">
	                    <div class="col-md-6">
	                        <h6 class="small text-muted">Estado</h6>
	                        <p id="detailsLoanStatus"></p>
	                    </div>
	                    <div class="col-md-6">
	                        <h6 class="small text-muted">Observación</h6>
	                        <p class="fw-bold" id="detailsLoanObservation"></p>
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
	
	<!-- Return Loan Modal -->
	<div class="modal fade" id="returnLoanModal" tabindex="-1" aria-labelledby="returnLoanModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	        	<!-- Modal Header -->
	            <div class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="returnLoanModalLabel">Confirmación</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <p id="modalMessage">¿Estás seguro de cambiar el estado a <span id="newStatus">Devuelto</span> de este libro?</p>
	            </div>
	            
	            <!-- Modal Footer -->
	            <div class="modal-footer">
	            	<!-- Cancel Button -->
	            	<button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	            	
	            	<!-- Confirm Button -->
					<button type="button" class="btn btn-custom-primary d-flex align-items-center" id="confirmReturn">
	                    <span id="confirmReturnIcon" class="me-2"><i class="bi bi-check2-square"></i></span>
	                    <span id="confirmReturnSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Confirmar
	                </button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<!-- Edit Loan Modal -->
	<div class="modal fade" id="editLoanModal" tabindex="-1" aria-labelledby="editLoanModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editLoanModalLabel">Editar Préstamo</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="editLoanForm" accept-charset="UTF-8" novalidate>
	                    <!-- Student and Loan Date Section -->
	                    <div class="row">
	                        <!-- Student Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLoanStudent" class="form-label">Estudiante <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editLoanStudent" 
	                                name="editLoanStudent" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Loan Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLoanDate" class="form-label">Fecha de Préstamo <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editLoanDate" 
	                                name="editLoanDate" 
	                                value="" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Return Date and Quantity Section -->
	                    <div class="row">
	                        <!-- Return Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editReturnDate" class="form-label">Fecha de Devolución <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editReturnDate" 
	                                name="editReturnDate" 
	                                value="" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Quantity Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLoanQuantity" class="form-label">Cantidad</label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="editLoanQuantity" 
	                                disabled
	                            >
	                        </div>
	                    </div>
	                    
	                    <!-- Observation Section -->
	                    <div class="row">
	                        <div class="col-md-12 mb-3">
	                            <label for="editloanObservation" class="form-label">Observación</label>
	                            <textarea 
	                                class="form-control" 
	                                id="editloanObservation" 
	                                name="editLoanObservation" 
	                                rows="1" 
	                                placeholder="Ingrese cualquier observación opcional"
	                            ></textarea>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Update Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editLoanForm" id="editLoanBtn">
	                    <span id="editLoanIcon" class="me-2"><i class="bi bi-floppy"></i></span>
	                    <span id="editLoanSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
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
		<jsp:param name="currentPage" value="loans.js" />
	</jsp:include>
</body>
</html>