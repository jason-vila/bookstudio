$(document).ready(function() {
	const currentPage = $('#sidebar').attr('data-current-page');

	$('.nav-link').each(function() {
		const $link = $(this);
		const $liElement = $link.closest('li');
		const $icon = $link.find('i');

		if ($link.attr('href') === currentPage) {
			$liElement.addClass('list-item-with-line');
			$link.addClass('active-effect');

			if ($icon.length) {
				const iconClass = $icon.attr('class').split(' ').pop();
				if (!iconClass.endsWith('-fill')) {
					$icon.removeClass(iconClass).addClass(iconClass + '-fill');
				}
			}
		} else {
			$liElement.removeClass('list-item-with-line');
			$link.removeClass('active-effect');
		}
	});
});