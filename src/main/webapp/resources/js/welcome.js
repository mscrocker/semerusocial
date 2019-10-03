let init = (baseURL) => {
	console.log("sadasfsaasfasf");
	let url = baseURL + "images/first";
	let params = {
		method: "GET"
	};
	
	if (checkLoggedIn()){
		authFetch(url, params, (result) => {
			result.json().then((body) => {
				if (body.imageId){
					window.location.href = baseURL + "carrusel/" + body.imageId;
				} else {
					window.location.href = baseURL + "addImage";
				}
				
			}).catch((error) => {
				console.log("ERROR");
			});
		}, (errors) => {
			console.log("ERROR");
		});
	} else {
		window.location.href = baseURL + "login";
	}
	
};