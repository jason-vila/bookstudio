function toggleButtonAndSpinner(action) {
    const button = $('#btnAdd');
    const spinner = $('#spinnerLoad');

    if (action === 'loading') {
        spinner.removeClass('d-none');
        button.prop('disabled', true);
    } else if (action === 'loaded') {
        spinner.addClass('d-none');
        button.prop('disabled', false);
    }
}

function setupDataTable(tableId) {
    $(tableId).DataTable({
        responsive: true,
        searching: true,
        lengthChange: true,
        paging: true,
        ordering: true,
        "columnDefs": [
            { "orderable": false, "targets": -1 }
        ],
        language: {
            search: "",
            searchPlaceholder: "Buscar...",
            lengthMenu: "Mostrar _MENU_ registros",
            info: "Mostrando _START_ a _END_ de _TOTAL_ registros",
            infoEmpty: "Mostrando 0 a 0 de 0 registros",
            infoFiltered: "(filtrado de _MAX_ registros totales)",
            zeroRecords: "No se encontraron registros coincidentes",
            emptyTable: "No hay datos disponibles en la tabla"
        },
        initComplete: function() {
            toggleButtonAndSpinner('loaded');
            $('#tableContainer').removeClass('d-none');
        }
    });
    
    setTimeout(function() {
        if ($('#spinnerLoad').is(':visible')) {
            toggleButtonAndSpinner('loaded');
            $('#tableContainer').removeClass('d-none');
        }
    }, 3000);
}