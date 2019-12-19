const carrusel = {
	PAGE_SIZE: 10,
	
	cachedImages: null,	
	hasNextImages: null,
	currentCachedPage: null,
	currentImageIndex: null,
	baseURL: null,
	
	nextEnabled: false,
	previousEnabled: false,
	
	loadingImageURL: null,
	city: null,
	
	baseInit:  function (baseURL) {
		this.baseURL = baseURL;
		this.loadingImageURL = document.getElementById("imgField").src;
		this.currentImageIndex = 0;
		this.queryPage(0);
	},
	
	authenticatedInit: function(baseURL) {
		this.baseInit(baseURL);
		let elements = document.getElementsByClassName("only-auth-elements");
		for (let i = 0; i < elements.length; i++){
			elements[i].classList.remove("hidden");
		}
	},
	
	anonymousInit: function(baseURL, city){
		this.city = city;
		this.baseInit(baseURL);
	},
	
	
	updateButtons: function(){
		this.previousEnabled = (this.currentCachedPage > 0) || (this.currentImageIndex > 0);
		this.nextEnabled = this.hasNextImages || (this.currentImageIndex < (this.cachedImages.length - 1));
		
		if (this.nextEnabled){
			document.getElementById("nextButton").classList.remove("btn-disabled");
			document.getElementById("nextButton").classList.add("btn-primary");
		} else {
			document.getElementById("nextButton").classList.remove("btn-primary");
			document.getElementById("nextButton").classList.add("btn-disabled");
		}
		if (this.previousEnabled){
			document.getElementById("previousButton").classList.remove("btn-disabled");
			document.getElementById("previousButton").classList.add("btn-primary");
		} else {
			document.getElementById("previousButton").classList.remove("btn-primary");
			document.getElementById("previousButton").classList.add("btn-disabled");
		}
	},
	
	queryPage: function(pageNumber){
		document.getElementById("imgField").src = this.loadingImageURL;
		this.currentCachedPage = pageNumber;
		let url = null;
		if (this.city === null){
			url = this.baseURL + "backend/images/carrusel?page=" + pageNumber;
		} else {
			url = this.baseURL + "backend/images/anonymousCarrusel?page=" + pageNumber + "&city=" + encodeURIComponent(this.city);
		}
		
		user.authFetch(url, {
			method: 'GET'
		}, (response) => {
			
			if (response.status !== 200){
				customAlert.showAlertFromResponse(response);
				return;
			}
			response.json().then((body) => {
				
				
				if (body.elements.length === 0){
					if (pageNumber === 0){
						if (this.city === null){
							window.location.href = this.baseURL + "addImage";
						} else {
							window.location.href = this.baseURL + "login";
						}
						
					} else {
						customAlert.showAlert({globalError: "Error: no image available"});
					}
					return;
				}
				
				this.cachedImages = body.elements;
				this.hasNextImages = body.existMoreElements;
				this.displayCachedImage(this.currentImageIndex);
				this.updateButtons();
			}).catch((errors) => {
				customAlert.showAlert("Internal server error");
			});
		}, (errors) => {
			customAlert.showAlert("Internal server error");
		});
		
		
		
	},
	
	displayCachedImage: function(index){
		this.currentImageIndex = index;
		
		// TODO: add format handling to images
		let metadata  = "data:image/jpeg" + this.cachedImages[this.currentImageIndex].type +   ";base64, ";
		
		//let metadata  = "data:image/jpeg" +  ";base64, ";
		document.getElementById("imgField").src = metadata + this.cachedImages[this.currentImageIndex].data;
	},
	
	nextImage: function(){
		if (!this.nextEnabled){
			return;
		}
		this.currentImageIndex++;
		if (this.currentImageIndex === this.cachedImages.length){
			// QUERY MORE IMAGES
			this.currentImageIndex = 0;
			this.queryPage(this.currentCachedPage + 1);
			
		} else {
			// MOVE TO THE NEXT IMAGE
			this.displayCachedImage(this.currentImageIndex);
		}
		this.updateButtons();
	},
	
	previousImage: function(){
		if (!this.previousEnabled){
			return;
		}
		this.currentImageIndex--;
		if (this.currentImageIndex < 0){
			// QUERY MORE IMAGES
			this.currentImageIndex = this.PAGE_SIZE - 1;
			this.queryPage(this.currentCachedPage - 1);
			
		} else {
			// MOVE TO THE PREVIOUS IMAGE
			this.displayCachedImage(this.currentImageIndex);
		}
		this.updateButtons();
	},
	
	
	deleteImage: function () {
		let imageID = this.cachedImages[this.currentImageIndex].imageId;
		
		
		let url = this.baseURL + "backend/images/remove/" + imageID;
		let params = {
				method: "DELETE"
		};
		user.authFetch(url, params, (resp) => {
			$("deleteModal").modal("hide");
			if (resp.status === 204){
				window.location.reload(false);
			} else {
			    customAlert.showAlertFromResponse(resp);
			}
		}, (errors) => {
			customAlert.showAlert("Internal server error");
		});
	}
	
	
};
