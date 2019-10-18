let counter = 0;
const initSuggestion = () => {
    	let controller = document.getElementById("imagesController");
    	if (controller.childElementCount === 0) {
    		//Do Something special when user doens't have any image
    	}
    	else {
    		controller.firstElementChild.classList.add("active");
    		//counter++;
    	}
    	
}

const onNextPressed = () => {
	let controller = document.getElementById("imagesController");
	if (controller.childElementCount === counter-1 && true) {
		//Fetch next page and append those images to the carrousel;
		//Show also a spinner in the meanwhile?
		
	}

	//At This poiunt we are sure we have more elements
	controller.children[counter].classList.remove("active");
	controller.children[++counter].classList.add("active");
}


const onPrevPressed = () => {
	let controller = document.getElementById("imagesController");
	if (counter === 0) {
		return;
	}
	controller.children[counter].classList.remove("active");
	controller.children[--counter].classList.add("active");
}