const loadImage = (baseURL) => {
	const path = window.location.pathname;
	const imageId = path.substring(
			path.lastIndexOf("/") + 1,
			path.length
	);
	const url = baseURL + "images/carrusel?page=" + imageId;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		console.log("EXITO!");
	}, (errors) => {
		console.log("ERROR!");
	});
};