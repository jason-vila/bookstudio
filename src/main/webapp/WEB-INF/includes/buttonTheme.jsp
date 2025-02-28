<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Set the dropdown direction based on the menu type -->
<c:set var="dropdownDirection" value="dropdown" />
<c:if test="${param.dropdownDirection != '1'}">
    <c:set var="dropdownDirection" value="dropup" />
</c:if>

<!-- Theme Selector Button -->
<div class="${dropdownDirection} bd-mode-toggle bd-navbar">
    <!-- Main Theme Toggle Button -->
    <button class="btn btn-link nav-link py-2 dropdown-toggle d-flex align-items-center link-body-emphasis"
            id="bd-theme"
            type="button"
            aria-expanded="false"
            data-bs-toggle="dropdown"
            data-bs-offset="0,0"
            aria-label="Toggle theme (auto)">
        <i class="bi my-1 theme-icon-active bi-circle-half"></i>
        <span class="visually-hidden" id="bd-theme-text">Cambiar tema</span>
    </button>

    <!-- Dropdown Menu with Theme Options -->
    <ul class="dropdown-menu gap-1 p-2 rounded-3 mx-0 shadow bg-body-secondary border" aria-labelledby="bd-theme-text">
        <!-- Light Theme Option -->
        <li class="mb-1">
            <button type="button" class="dropdown-item rounded-2 d-flex align-items-center" 
                    data-bs-theme-value="light" data-bs-icon="bi-sun" aria-pressed="false">
                <i class="bi me-2 bi-sun"></i>
                Claro
                <i class="bi ms-auto d-none bi-check2"></i>
            </button>
        </li>

        <!-- Dark Theme Option -->
        <li class="mb-1">
            <button type="button" class="dropdown-item rounded-2 d-flex align-items-center" 
                    data-bs-theme-value="dark" data-bs-icon="bi-moon" aria-pressed="false">
                <i class="bi me-2 bi-moon"></i>
                Oscuro
                <i class="bi ms-auto d-none bi-check2"></i>
            </button>
        </li>

        <!-- System Theme Option -->
        <li>
            <button type="button" class="dropdown-item rounded-2 d-flex align-items-center active" 
                    data-bs-theme-value="auto" data-bs-icon="bi-circle-half" aria-pressed="true">
                <i class="bi me-2 bi-circle-half"></i>
                Sistema
                <i class="bi ms-auto d-none bi-check2"></i>
            </button>
        </li>
    </ul>
</div>