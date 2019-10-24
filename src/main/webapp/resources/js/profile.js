let profile = null;
 profile = {

	UserData: {
		'birthDate' : null,
		'sex' : null, 
		'city' : null,
		'description': null
	},
	
	clearStatusIcons: () => {
		document.getElementById("profileLoading").classList.add("hidden");
		document.getElementById("profileFinished").classList.add("hidden");
		document.getElementById("profileError").classList.add("hidden");
	},
	
	notifyChange: (name) => {
		profile.clearStatusIcons();
		let result = false;
		switch (name) {
			case "birthDate": 
				result = profile.validation.validateBirthDate(true);
				break;
			case "description":
				result = profile.validation.validateDescription(true);
				break;
			case "sex":
				result = profile.validation.validateSex(true);
				break;
			case "city":
				result = profile.validation.validateCity(true);
				break;
		}

		document.getElementById("profileButton").disabled = !result;
	},
	
	clearFeedback: (name) => {
		document.getElementById(name).classList.remove("is-valid");
		document.getElementById(name).classList.remove("is-invalid");
	},
	

	validation: {
		clearAllFields: () => {
			validation.clearField(document.getElementById("birthday"));
			validation.clearField(document.getElementById("sexField"));
			validation.clearField(document.getElementById("cityField"));
			validation.clearField(document.getElementById("descriptionField"));
		},
		
		validateBirthDate: (updateField) => {
			let age = new Date(new Date() - new Date(document.getElementById("birthday").value)).getFullYear() - 1970;
			let result = (age >= 18);
			if (updateField === true){
				validation.updateField(document.getElementById("birthday"), result);
			}
			return result;
		},
		
		validateDescription: (updateField) => {
			let description = document.getElementById("descriptionField").value;
			let result = (description !== "") && (description !== undefined) && (description !== null);
			if (updateField === true){
				validation.updateField(document.getElementById("descriptionField"), result);
			}
			return result;
		},
		
		validateSex: (updateField) => {
			let sex = document.getElementById("sexField").value;
			let result = ["Male", "Female", "Other"].includes(sex);
			if (updateField === true){
				validation.updateField(document.getElementById("sexField"), result);
			}
			return result;
		},
		
		validateCity: (updateField) => {
			let city = document.getElementById("cityField").value;
			let result = (city !== "") && (city !== undefined) && (city !== null);
			if (updateField === true){
				validation.updateField(document.getElementById("cityField"), result);
			}
			return result;
		}
	},
	

	updateCriteria: () => {
		document.getElementById("profileButton").disabled = true;
		profile.clearStatusIcons();
		let date = new Date(document.getElementById("birthday").value);
		let profileData = {
			day: date.getUTCDate(),
			month: date.getUTCMonth(),
			year: date.getUTCFullYear(),
			description: document.getElementById("descriptionField").value,
			city: document.getElementById("cityField").value,
			sex: document.getElementById("sexField").value
		};
		
		let url = criteriaSettings.baseURL + "backend/users/updateProfile";
		let params = {
			method: 'PUT',
			body: JSON.stringify(profileData),
			headers: { "Content-Type": "application/json" }
		};
		user.authFetch(url, params, (response) => {
			profile.validation.clearAllFields();
			if (response.status !== 204){
				customAlert.showAlertFromResponse(response);
				profile.clearStatusIcons();
				document.getElementById("profileError").classList.remove("hidden");
			} else {
				profile.clearStatusIcons();
				document.getElementById("profileFinished").classList.remove("hidden");
			}
			
		}, (errors) => {
			customAlert.showAlert(errors);
		});
	},

	initProfile: (baseURL) => {
		profile.clearStatusIcons();
		const url = baseURL + "backend/users/data" ;
		if (user.checkLoggedIn()){
			
			document.getElementById("birthday").onchange = () => profile.notifyChange("birthDate");
			document.getElementById("descriptionField").onchange = () => profile.notifyChange("description");
			document.getElementById("sexField").onclick = () => profile.notifyChange("sex");
			document.getElementById("cityField").onclick = () => profile.notifyChange("city");
			
			document.getElementById("profileButton").onclick = () => profile.updateCriteria();
			
			user.authFetch(url, {method: 'GET'}, (response) => {	
				if (response.status !== 200){
					console.log("ERROR!");
					return;
				}
				response.json().then((body) => {
					profile.UserData.birthDate = new Date();
					profile.UserData.birthDate.setUTCFullYear(body.date.year);
					profile.UserData.birthDate.setUTCMonth(body.date.monthValue - 1);
					profile.UserData.birthDate.setUTCDate(body.date.dayOfMonth);
					
					profile.UserData.sex = body.sex;
					profile.UserData.city = body.city;
					profile.UserData.description = body.description;
					
					document.getElementById("birthday").value = profile.UserData.birthDate.toISOString().substr(0,10);
					document.getElementById("cityField").value = ""+body.city;
					document.getElementById("sexField").value = ""+body.sex;
					document.getElementById("descriptionField").value = ""+body.description;
				}
				);
			});
		}else {
			window.location.href = baseURL + "login";
		}
	},
	
	initProfileDisplay: (baseURL) => {
		const url = baseURL + "backend/users/data" ;
		if (user.checkLoggedIn()){
			
			
			user.authFetch(url, {method: 'GET'}, (response) => {	
				if (response.status !== 200){
					console.log("ERROR!");
					return;
				}
				response.json().then((body) => {
					document.getElementById("birthday").innerText = ""+body.age;
					document.getElementById("cityField").innerText = ""+body.city;
					document.getElementById("sexField").innerText = ""+body.sex;
					document.getElementById("descriptionField").innerText = ""+body.description;
				});
			});
		}else {
			window.location.href = baseURL + "login";
		}
	}

	
};
