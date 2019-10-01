var Base64Binary = {
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
	
	/* will return a  Uint8Array type */
	decodeArrayBuffer: function(input) {
		var bytes = (input.length/4) * 3;
		var ab = new ArrayBuffer(bytes);
		this.decode(input, ab);
		
		return ab;
	},

	removePaddingChars: function(input){
		var lkey = this._keyStr.indexOf(input.charAt(input.length - 1));
		if(lkey == 64){
			return input.substring(0,input.length - 1);
		}
		return input;
	},

	decode: function (input, arrayBuffer) {
		//get last chars to see if are valid
		input = this.removePaddingChars(input);
		input = this.removePaddingChars(input);

		var bytes = parseInt((input.length / 4) * 3, 10);
		
		var uarray;
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		var j = 0;
		
		if (arrayBuffer)
			uarray = new Uint8Array(arrayBuffer);
		else
			uarray = new Uint8Array(bytes);
		
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		
		for (i=0; i<bytes; i+=3) {	
			//get the 3 octects in 4 ascii chars
			enc1 = this._keyStr.indexOf(input.charAt(j++));
			enc2 = this._keyStr.indexOf(input.charAt(j++));
			enc3 = this._keyStr.indexOf(input.charAt(j++));
			enc4 = this._keyStr.indexOf(input.charAt(j++));
	
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
	
			uarray[i] = chr1;			
			if (enc3 != 64) uarray[i+1] = chr2;
			if (enc4 != 64) uarray[i+2] = chr3;
		}
	
		return uarray;	
	}
};

var loadedImage = false;


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
	if (result.status !== 200){
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
	const rawImageData = image.substring(23,image.length);  
	
	authFetch(uploadURL, {
		method: "POST",
		body: JSON.stringify({
			data: [].slice.call(Uint8Array.from(atob(rawImageData), c => c.charCodeAt(0))),
			description: description
		}),
		headers: { "Content-Type": "application/json" }
	}, finishUploadImage, finishUploadWithErrors);
};

const init = (fileDragArea, filePreviewArea, uploadButton, uploadURL) => {
	fileDragArea.addEventListener("drop", (event) => dropImage(event, filePreviewArea), false);
	window.addEventListener("dragover",function(e){
	  e = e || event;
	  e.preventDefault();
	},false);
	window.addEventListener("drop",function(e){
	  e = e || event;
	  e.preventDefault();
	},false);
	
	uploadButton.addEventListener("click", () => uploadImage(
		document.getElementById("descriptionInput").value,
		loadedImage ? document.getElementById("filePreviewArea").src : undefined,
		uploadURL
	));
};

