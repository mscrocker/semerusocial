
const addImage = {
	loadedImage: false,
	previewFile:  (preview, fileInput) => {
		 var encoding = ["png","jpeg","jpg"];
		 var file    = fileInput.files[0];
		  if (!encoding.includes(file.name.split(".")[1]))
			  {
			  	showAlert("Not a supported image type!");
			  	preview.src = "";
			  	return;
			  }
		  customAlert.hideAlert();
		  var reader  = new FileReader();

		  reader.onloadend = function () {
		    preview.src = reader.result;
		    addImage.loadedImage = true;
		  };

		  if (file) {
		    reader.readAsDataURL(file);
		  } else {
		    // preview.src = "";
		  }
	},
	
	dropImage: (event, previewImageArea) => {
		var files = event.target.files || event.dataTransfer.files;
		for (let i = 0; i < files.length; i++){
			let fr = new FileReader();
			fr.readAsDataURL(files[i]);
			fr.onload = (data) => {
				previewImageArea.src = data.target.result;
			};
		}
		addImage.loadedImage = true;
	},
	
	finishUploadImage: (result, baseURL) => {
		if (result.status !== 201){
			customAlert.showAlertFromResponse(result);
			document.getElementById("uploadButton").disabled = false;
			return;
		}
		result.json().then((body) => {
			
			document.getElementById("uploadButton").disabled = false;
			customAlert.hideAlert();
			window.location.href = baseURL + "carrusel";
		}).catch((errors) => {
			showAlert("Error uploading image");
		});
		
	},
	finishUploadWithErrors: (errors) => {
		customAlert.showAlert("Error uploading image");
	},
	uploadImage: (image, baseURL) => {
		customAlert.hideAlert();
		if (image === undefined){
			customAlert.showAlert("Error: image is mandatory.");
			return;
		}
		
		document.getElementById("uploadButton").disabled = true;
		
		
		user.authFetch(baseURL + "backend/images/add", {
			method: "POST",
			body: JSON.stringify({
				data: image
			}),
			headers: { "Content-Type": "application/json" }
		}, (result) => addImage.finishUploadImage(result, baseURL), addImage.finishUploadWithErrors);
	},
	initAddImage: (filePreviewArea, uploadButton,uploadFile, baseURL) => {
		filePreviewArea.addEventListener("drop", (event) => addImage.dropImage(event, filePreviewArea), false);
		window.addEventListener("dragover",function(e){
		  e = e || event;
		  e.preventDefault();
		},false);
		window.addEventListener("drop",function(e){
		  e = e || event;
		  e.preventDefault();
		},false);
		
		uploadFile.addEventListener("change", () => addImage.previewFile(filePreviewArea,uploadFile));

		
		uploadButton.addEventListener("click", () => addImage.uploadImage(
			addImage.loadedImage ? document.getElementById("filePreviewArea").src : undefined,
			baseURL
		));
	}
};

	

