let previousLink = null;
let nextLink = null;

const getImageId = () => {
	return window.location.pathname.substring(
			window.location.pathname.lastIndexOf("/") + 1,
			window.location.pathname.length
	);
};

const getImageIdUrl = () => {
	let baseURL = "[[@{'/'}]]";
	let url = baseURL + "backend/images/first";
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

const loadImage = (baseURL) => {
	const imageId = getImageId();
	const url = baseURL + "backend/images/carrusel/" + imageId;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		
		if (response.status !== 200){
			console.log("ERROR!");
			return;
		}
		response.json().then((body) => {

			
			// data + type -> string
			
			let metadata  = "data:image/" +body.image.type +   ";base64, ";
			document.getElementById("imgField").src = metadata + body.image.data;
			
			let baseLink = baseURL + "carrusel/";
			if (body.nextId){
				nextLink = baseLink + body.nextId;
				document.getElementById("nextButton").classList.add("btn-primary");
			} else {
				document.getElementById("nextButton").classList.add("btn-disabled");
			}
			if (body.prevId){
				previousLink = baseLink + body.prevId;
				document.getElementById("previousButton").classList.add("btn-primary");
			} else {
				document.getElementById("previousButton").classList.add("btn-disabled");
			}
			
			
		});
		
	}, (errors) => {
		console.log("ERROR!");
	});
};

const getNextLink = (baseURL) => {
	if (previousLink !== null){
		return previousLink;
	}
	if (nextLink !== null){
		return nextLink;
	}
	
	return baseURL + "addImage";
	
};

const deleteImage = (baseURL, imageID) => {
	let url = baseURL + "backend/images/remove/" + imageID;
	let params = {
			method: "DELETE"
	};
	authFetch(url, params, (resp) => {
		$("deleteModal").modal("hide");
		if (resp.status === 204){
			console.log("SUCCESS!");
			window.location.href = getNextLink(baseURL);
		} else {
			console.log("ERROR");
			
		}
	}, (errors) => {
		console.log("ERROR");
	});
};
