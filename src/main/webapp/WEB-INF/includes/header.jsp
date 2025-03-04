<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header position-fixed d-flex align-items-center px-3 bg-body-secondary border-bottom">

    <!-- Menu Button (Visible on Mobile) -->
    <button class="btn-menu nav-link d-md-none" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasSidebar" aria-controls="offcanvasSidebar">
        <i class="bi bi-list header-icon"></i>
    </button>

    <!-- Container for Logo and User Controls -->
    <div class="d-flex flex-grow-1 justify-content-between align-items-center">
    
        <!-- Empty Space for Alignment (Visible on Mobile) -->
        <div class="d-md-none me-5"></div>

        <!-- Logo -->
		<a href="/bookstudio/dashboard.jsp" class="text-center text-decoration-none text-body-emphasis ms-md-2 d-flex align-items-center">
		    <img class="logo me-2" alt="Logo" src="images/logo-light.png">
		    <span class="fs-5 text-logo">BookStudio</span>
		</a>

        <!-- Container for Theme Toggle and User Dropdown -->
        <div class="d-flex align-items-center">
        
            <!-- Theme Toggle Button -->
            <div class="dropdown me-2">
                <jsp:include page="buttonTheme.jsp">
                	<jsp:param name="dropdownDirection" value="1" />
                </jsp:include>
            </div>
            
            <!-- Separator (Visible on Large Screens) -->
			<div class="d-flex align-items-center me-2">
			    <div class="vr d-none d-lg-block mx-2 header-separator"></div>
			</div>

            <!-- User Dropdown -->
            <div class="dropdown bd-navbar">
                <button type="button" class="btn btn-link nav-link d-flex align-items-center link-body-emphasis" data-bs-toggle="dropdown" aria-expanded="false">
                    <c:choose>
					    <c:when test="${not empty sessionScope.user_profile_image}">
					        <img src="${sessionScope.user_profile_image}" alt="Foto" width="24" height="24" class="rounded-circle">
					    </c:when>
					    <c:otherwise>
					        <i class="bi pe-none bi-person-circle header-icon"></i>
					    </c:otherwise>
					</c:choose>
                </button>
                
                <!-- Dropdown Menu -->
                <ul class="dropdown-menu dropdown-menu-end text-small gap-1 p-2 rounded-3 mx-0 shadow bg-body-secondary border">
                    <li>
                    	<!-- Profile Button -->
                    	<a class="dropdown-item rounded-2 d-flex align-items-center" href="profile.jsp">
                    		<i class="bi bi-person me-2"></i>
                    		Perfil
                    	</a>
                    </li>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                    	<!-- Logout Button -->
                    	<button class="dropdown-item rounded-2 dropdown-danger d-flex align-items-center" data-bs-toggle="modal" data-bs-target="#logoutModal">
                    		<i class="bi bi-box-arrow-right me-2"></i>
                    		Cerrar Sesión
                    	</button>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</header>

<!-- Logout Confirmation Modal -->
<div class="modal fade" id="logoutModal" tabindex="-1" aria-labelledby="logoutModalLabel" aria-hidden="true" data-bs-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h5 class="modal-title text-body-emphasis" id="logoutModalLabel">Confirmar Cierre de Sesión</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            
            <!-- Modal Body -->
            <div class="modal-body">
                <p>¿Estás seguro de que deseas cerrar tu sesión?</p>
            </div>
            
            <!-- Modal Footer -->
            <div class="modal-footer">
                <!-- Cancel Button -->
                <button type="button" class="btn btn-custom-secondary d-flex align-items-center" data-bs-dismiss="modal">
		        	Cancelar
		        </button>
		           
                <!-- Logout Form -->
                <form action="LoginServlet" method="post">
                    <input type="hidden" name="type" value="logout"/>
                    
                    <!-- Logout Button -->
                    <button type="submit" class="btn btn-danger d-flex align-items-center">
                    	<i class="bi bi-box-arrow-right me-2"></i>
                    	Cerrar Sesión
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Session Expiration Confirmation Modal -->
<div class="modal fade" id="sessionExpiredModal" tabindex="-1" aria-labelledby="sessionExpiredModalLabel" aria-hidden="true" data-bs-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h5 class="modal-title" id="sessionExpiredModalLabel">Sesión por expirar</h5>
            </div>
            
            <!-- Modal Body -->
            <div class="modal-body">
                <p>Tu sesión está a punto de expirar. ¿Deseas continuar con tu sesión?</p>
            </div>
            
            <!-- Modal Footer -->
            <div class="modal-footer">
            	<!-- Close Session Button -->
                <button type="button" class="btn btn-danger d-flex align-items-center" id="logoutBtn">
                    <span id="logoutIcon" class="me-2"><i class="bi bi-box-arrow-right"></i></span>
                    <span id="logoutSpinner" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
                    Cerrar sesión
                </button>
                
                <!-- Continue Session Button -->
                <button type="button" class="btn btn-custom-secondary d-flex align-items-center" id="extendSessionBtn" data-bs-dismiss="modal">
                    <i class="bi bi-arrow-clockwise me-2"></i>
                    Continuar sesión
                </button>
            </div>
        </div>
    </div>
</div>