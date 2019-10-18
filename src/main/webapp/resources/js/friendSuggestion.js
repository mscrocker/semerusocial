let counter = 0;
let page = 0;
let PAGE_SIZE = 10;
let askForMore = true;
let baseUrl = "";
const initSuggestion = (Url) => {
		 baseUrl = Url;
    	let controller = document.getElementById("imagesController");
    	if (controller.childElementCount < PAGE_SIZE) {
    		askForMore = false;
    		//    		Do Something special when user doens't have any image
    	}
    	if (controller.childElementCount === 0) {
    		//Do Something special when user doens't have any image
    	}
    	
    	else {
    		controller.firstElementChild.classList.add("active");
    		//counter++;
    	}
    	
}
const loadImages = (baseUrl) => {

	const url = baseUrl + "backend/images/carrusel/user/1" + "?page="+ page;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		
		if (response.status !== 200){
			console.log("ERROR!");
			return;
		}
		response.json().then((body) => {
				
//			//block -> {
//			long:imageId
//			String data;
//			String type;
//		}
			let images = body.images;
			let exists = body.existMoreImages;
			if (!exists) 
				askForMore = false;
			
	    	let controller = document.getElementById("imagesController");
    		
	     	images.forEach( image => {
	    		let div = document.createElement("div");
	    		let img = document.createElement("img");
	    		div.classList.add("carousel-item");
	    		img.classList.add("d-block");
	    		img.classList.add("mx-auto");
	    		img.src = "data:image/" +image.type +   ";base64, " + image.data;
	    		div.append(img);
	    		controller.append(div);
	    	});
	     	if (images.length >= 1){
	    		controller.children[counter].classList.remove("active");
	controller.children[++counter].classList.add("active");

	    	}
			// data + type -> string
			
		});
		
	}, (errors) => {
		console.log("ERROR!");
	});
};
const onNextPressed = (baseUrl) => {
	let controller = document.getElementById("imagesController");
	if (controller.childElementCount === counter+1 && true) {
		if(!askForMore)
			return;
		let spinner = document.getElementById("loader");
		spinner.classList.remove("hidden");
		page++;
		loadImages(baseUrl);
		spinner.classList.add("hidden");

		//Fetch next page and append those images to the carrousel;
		//Show also a spinner in the meanwhile?
		
	}
	else {

	//At This poiunt we are sure we have more elements
	controller.children[counter].classList.remove("active");
	controller.children[++counter].classList.add("active");
	}
}


const onPrevPressed = () => {
	let controller = document.getElementById("imagesController");
	if (counter === 0) {
		return;
	}
	controller.children[counter].classList.remove("active");
	controller.children[--counter].classList.add("active");
}