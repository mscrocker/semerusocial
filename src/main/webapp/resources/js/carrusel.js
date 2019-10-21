const carrusel = {
	previousLink: null,
	nextLink: null,

	getImageId: () => {
		return window.location.pathname.substring(
				window.location.pathname.lastIndexOf("/") + 1,
				window.location.pathname.length
		);
	},

	getImageIdUrl: () => {
		let baseURL = "[[@{'/'}]]";
		let url = baseURL + "backend/images/first";
		let params = {
			method: "GET"
		};
		
		if (user.checkLoggedIn()){
			user.authFetch(url, params, (result) => {
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
	},

	loadImage: (baseURL) => {
		const imageId = carrusel.getImageId();
		const url = baseURL + "backend/images/carrusel/" + imageId;
		user.authFetch(url, {
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
	},

	getNextLink: (baseURL) => {
		if (previousLink !== null){
			return previousLink;
		}
		if (nextLink !== null){
			return nextLink;
		}
		
		return baseURL + "addImage";
		
	},

	deleteImage: (baseURL, imageID) => {
		let url = baseURL + "backend/images/remove/" + imageID;
		let params = {
				method: "DELETE"
		};
		user.authFetch(url, params, (resp) => {
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
	},

	finishEditImage: (result) => {
		if (result.status !== 204){
			showAlert("Error uploading image");
			return;
		}
	},

	finishEditWithErrors: (errors) => {
		showAlert("Error uploading image");
	},

	editImage: (baseURL, imageID, description) => {
		$("#editModal").modal("hide");
		if ((description === undefined) || (description === null) || (description === "")){
			showAlert("Error: description is mandatory.");
			return;
		}
		let url = baseURL + "backend/images/edit/" + imageID;
		user.authFetch(url, {
			method: "PUT",
			body: JSON.stringify({
				description: description
			}),
			headers: { "Content-Type": "application/json" }
		}, finishEditImage, finishEditWithErrors);
		document.getElementById('descriptionField').innerText=description;
	}

};
