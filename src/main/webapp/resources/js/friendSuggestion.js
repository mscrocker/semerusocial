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
    		// Do Something special when user doens't have any image
    	}
    	if (controller.childElementCount === 0) {
    		// Do Something special when user doens't have any image
    	}
    	
    	else {
    		controller.firstElementChild.classList.add("active");
    		// counter++;
    	}
    	
};
const loadImages = (baseUrl) => {
    	// Show a spinner while we load it
	let spinner = document.getElementById("loader");
	// Hide the next button till we process the request
	let next = document.getElementById("nextButton");
	 next.classList.add("hidden");
	spinner.classList.remove("hidden");
	 // Todo , get userId of suggestedUser
	const url = baseUrl + "backend/images/carrusel/user/1" + "?page="+ page;
	authFetch(url, {
		method: 'GET'
	}, (response) => {
		
		if (response.status !== 200){
			console.log("ERROR!");
			return;
		}
		response.json().then((body) => {
				

			let images = body.images;
			let exists = body.existMoreImages;
			// If we dont have more images we make sure we dont make
			// more petitions
			if (!exists) 
				askForMore = false;
			
	    	let controller = document.getElementById("imagesController");
    		// Add the images to the DOM
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
	     	// Only if we received more images we proceed
	     	// Move to the next image in case we had at least one more
	     	if (images.length >= 1){
	    		controller.children[counter].classList.remove("active");
	    		controller.children[++counter].classList.add("active");
	    		
	    		if (images.length !== 1){
	    			next.classList.remove("hidden");
		     	}
	    	}
			// data + type -> string
			spinner.classList.add("hidden");
		});
		
	}, (errors) => {
		console.log("ERROR!");
	});
};
const onNextPressed = (baseUrl) => {
	let controller = document.getElementById("imagesController");
	// Case when we move from the first Slide
	if (counter=== 0){
		let prev = document.getElementById("previousButton");
		prev.classList.remove("hidden");
	}
	// Case when we are returning to the last slide and we know is the last
	// one
	if (counter+2 == controller.childElementCount && !askForMore){
		let next = document.getElementById("nextButton");
	next.classList.add("hidden");
}
	// When we reach the last element
	if (controller.childElementCount === counter+1 && true) {
		if(!askForMore)
			return;
		page++;
		loadImages(baseUrl);

		// Fetch next page and append those images to the carrousel;
		// Show also a spinner in the meanwhile?
		
	}
	else {

	// At This poiunt we are sure we have more elements
	controller.children[counter].classList.remove("active");
	controller.children[++counter].classList.add("active");
	}
};


const onPrevPressed = () => {
	let controller = document.getElementById("imagesController");
	// Case where we are returning to the initial slide
	if (counter === 1  ) {
		let prev = document.getElementById("previousButton");
		prev.classList.add("hidden");

	}
	// Case where we are coming from the last slide
	if (counter === controller.childElementCount -1  ) {
		let next = document.getElementById("nextButton");
		next.classList.remove("hidden");
	}
	controller.children[counter].classList.remove("active");
	controller.children[--counter].classList.add("active");
};