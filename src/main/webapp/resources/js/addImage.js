
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
		  showAlert(null);
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

		result.json().then((body) => {
			if (result.status !== 201){
				showAlert(body);
				document.getElementById("uploadButton").disabled = false;
				return;
			}
			document.getElementById("uploadButton").disabled = false;
			showAlert(null);
			window.location.href = baseURL + "carrusel/" + body.imageId;
		}).catch((errors) => {
			showAlert("Error uploading image");
		});
		
	},
	finishUploadWithErrors: (errors) => {
		showAlert("Error uploading image");
	},
	uploadImage: (description, image, baseURL) => {
		showAlert(null);
		if ((description === undefined) || (description === null) || (description === "")){
			showAlert("Error: description is mandatory.");
			return;
		}
		if (image === undefined){
			showAlert("Error: image is mandatory.");
			return;
		}
		
		document.getElementById("uploadButton").disabled = true;
		
		
		user.authFetch(baseURL + "backend/images/add", {
			method: "POST",
			body: JSON.stringify({
				data: image,
				description: description
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
			document.getElementById("descriptionInput").value,
			addImage.loadedImage ? document.getElementById("filePreviewArea").src : undefined,
			baseURL
		));
	}
};

	

