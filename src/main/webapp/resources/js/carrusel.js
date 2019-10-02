let previousLink = null;
let nextLink = null;

const loadImage = (baseURL) => {
	const path = window.location.pathname;
	const imageId = path.substring(
			path.lastIndexOf("/") + 1,
			path.length
	);
	const url = baseURL + "images/carrusel/" + imageId;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		
		if (response.status !== 200){
			console.log("ERROR!");
			return;
		}
		response.json().then((body) => {
			/*
			let data = eval(body.image.data);
			var blob = new Blob( [ data ], { type: "image/jpeg" } );
			var imageUrl = URL.createObjectURL( blob );
			*/
			
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
			nextLink = baseLink + body.nextId;
			previousLink = baseLink + body.prevId;
		});
		
	}, (errors) => {
		console.log("ERROR!");
	});
};