
var loadedImage = false;

const previewFile  =  (preview, fileInput) => {
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
	    loadedImage = true;
	  };

	  if (file) {
	    reader.readAsDataURL(file);
	  } else {
	    // preview.src = "";
	  }
	};

const dropImage = (event, previewImageArea) => {
	var files = event.target.files || event.dataTransfer.files;
	for (let i = 0; i < files.length; i++){
		let fr = new FileReader();
		fr.readAsDataURL(files[i]);
		fr.onload = (data) => {
			previewImageArea.src = data.target.result;
		};
	}
	loadedImage = true;
};


const finishUploadImage = (result) => {
	if (result.status !== 201){
		showAlert("Error uploading image");
		return;
	}
};

const finishUploadWithErrors = (errors) => {
	showAlert("Error uploading image");
};

const uploadImage = (description, image, uploadURL) => {
	showAlert(null);
	if ((description === undefined) || (description === null) || (description === "")){
		showAlert("Error: description is mandatory.");
		return;
	}
	if (image === undefined){
		showAlert("Error: image is mandatory.");
		return;
	}
	
	authFetch(uploadURL, {
		method: "POST",
		body: JSON.stringify({
			data: image,
			description: description
		}),
		headers: { "Content-Type": "application/json" }
	}, finishUploadImage, finishUploadWithErrors);
};

const initAddImage = (filePreviewArea, uploadButton,uploadFile, uploadURL) => {
	filePreviewArea.addEventListener("drop", (event) => dropImage(event, filePreviewArea), false);
	window.addEventListener("dragover",function(e){
	  e = e || event;
	  e.preventDefault();
	},false);
	window.addEventListener("drop",function(e){
	  e = e || event;
	  e.preventDefault();
	},false);
	
	uploadFile.addEventListener("change", () => previewFile(filePreviewArea,uploadFile));

	
	uploadButton.addEventListener("click", () => uploadImage(
		document.getElementById("descriptionInput").value,
		loadedImage ? document.getElementById("filePreviewArea").src : undefined,
		uploadURL
	));
};

	

