<%@page import="com.bookstudio.utils.LoginConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String currentPage = (String) request.getParameter("currentPage");
	String role = (String) session.getAttribute(LoginConstants.ROLE);
	
	out.println("<script>sessionStorage.setItem('userRole', '" + role + "');</script>");
%>

<!-- Sidebar (Visible on Medium and Large Screens) -->
<aside class="position-fixed d-none d-md-flex flex-column border-end bg-body-secondary"
       id="sidebar" data-current-page="<%= currentPage %>">
    <!-- Navigation Links -->
    <ul class="nav flex-column p-3 flex-grow-1">
        <li class="mb-1">
            <a href="dashboard.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-bar-chart"></i>
                Dashboard
            </a>
        </li>
        <li class="mb-1">
            <a href="loans.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-file-earmark-text"></i>
                Préstamos
            </a>
        </li>
        <li class="mb-1">
            <a href="books.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-journal-bookmark"></i>
                Libros
            </a>
        </li>
        <li class="mb-1">
            <a href="authors.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-person-plus"></i>
                Autores
            </a>
        </li>
        <li class="mb-1">
            <a href="publishers.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-map"></i>
                Editoriales
            </a>
        </li>
        <li class="mb-1">
            <a href="courses.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-stickies"></i>
                Cursos
            </a>
        </li>
        <li class="mb-1">
            <a href="students.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2  bi-mortarboard"></i>
                Estudiantes
            </a>
        </li>
        <c:if test="${role == 'administrador'}">
	        <li class="mb-1">
	            <a href="users.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
	                <i class="bi pe-none me-2  bi-people"></i>
	                Usuarios
	            </a>
	        </li>
	    </c:if>
    </ul>

    <!-- User Information Footer -->
    <footer class="p-4 mt-auto">
        <p class="text-muted mb-0">Sesión iniciada como: <span class="text-body-emphasis">${sessionScope.username}</span></p>
    </footer>
</aside>

<!-- Sidebar (Offcanvas - Visible on Small Screens) -->
<aside class="offcanvas offcanvas-start border-end bg-body-secondary" id="offcanvasSidebar" aria-labelledby="offcanvasSidebarLabel">
    <!-- Offcanvas Header -->
    <header class="offcanvas-header">
        <h4 id="offcanvasSidebarLabel">BookStudio</h4>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
    </header>

    <!-- Offcanvas Navigation -->
    <nav class="offcanvas-body d-flex flex-column p-0">
        <ul class="nav flex-column p-3 flex-grow-1">
            <li class="mb-1">
                <a href="dashboard.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-bar-chart"></i>
                    Dashboard
                </a>
            </li>
            <li class="mb-1">
                <a href="loans.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-file-earmark-text"></i>
                    Préstamos
                </a>
            </li>
            <li class="mb-1">
                <a href="books.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-journal-bookmark"></i>
                    Libros
                </a>
            </li>
            <li class="mb-1">
                <a href="authors.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-person-plus"></i>
                    Autores
                </a>
            </li>
            <li class="mb-1">
                <a href="publishers.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-map"></i>
                    Editoriales
                </a>
            </li>
            <li class="mb-1">
                <a href="courses.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-stickies"></i>
                    Cursos
                </a>
            </li>
            <li class="mb-1">
                <a href="students.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-mortarboard"></i>
                    Estudiantes
                </a>
            </li>
            <c:if test="${role == 'administrador'}">
	            <li class="mb-1">
	                <a href="users.jsp" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
	                    <i class="bi pe-none me-2 bi-people"></i>
	                    Usuarios
	                </a>
	            </li>
            </c:if>
        </ul>

        <!-- User Information Footer -->
        <footer class="p-4 mt-auto">
            <p class="text-muted mb-0">Sesión iniciada como: <span class="text-body-emphasis">${sessionScope.username}</span></p>
        </footer>
    </nav>
</aside>