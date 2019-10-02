let previousLink = null;
let nextLink = null;

const getImageId = () => {
	return window.location.pathname.substring(
			window.location.pathname.lastIndexOf("/") + 1,
			window.location.pathname.length
	);
};

const loadImage = (baseURL) => {
	const imageId = getImageId();
	const url = baseURL + "images/carrusel/" + imageId;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		
		if (response.status !== 200){
			console.log("ERROR!");
			return;
		}
		response.json().then((body) => {

			
			/*jshint -W061 */
			let oriData = eval(body.image.data);
			let tmpArray = new Uint8Array(oriData);
			
			
			let stringDecoded = tmpArray.reduce((acu, val) => {
				return acu + String.fromCharCode(val);
			}, "");
			
			var encoded = btoa(stringDecoded);
			document.getElementById("imgField").src = "data:image/jpeg;base64, " + encoded;
			document.getElementById("descriptionField").innerText = body.image.description;
			
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
	let url = baseURL + "images/remove/" + imageID;
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
