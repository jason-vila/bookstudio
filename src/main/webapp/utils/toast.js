function showToast(message, type) {
	const $toastContainer = $('#toast-container');

	$toastContainer.find('.toast').toast('hide').remove();

	const borderClasses = type === 'success'
		? 'text-success-emphasis bg-success-subtle border border-success-subtle rounded-2'
		: 'text-danger-emphasis bg-danger-subtle border border-danger-subtle rounded-2';

	const $toastElement = $(`
	<div class="toast align-items-center ${borderClasses}" role="alert" aria-live="assertive" aria-atomic="true">
		<div class="d-flex">
			<div class="toast-body d-flex align-items-center">
				<i class="bi ${type === 'success' ? 'bi-check2-circle' : 'bi-x-circle'} me-2"></i>${message}
			</div>
			<button type="button" class="btn-close ${type === 'success' ? 'btn-close-success' : 'btn-close-danger'} me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
	`);

	$toastContainer.append($toastElement);

	const bootstrapToast = new bootstrap.Toast($toastElement[0], { animation: true, autohide: true, delay: 3000 });
	bootstrapToast.show();

	$toastElement.on('hidden.bs.toast', function() {
		$(this).remove();
	});
}