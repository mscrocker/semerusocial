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
			case "premium":
				result = true;
				break;
		}

		document.getElementById("profileButton").disabled = !result;
	},
	
	clearFeedback: (name) => {
		document.getElementById(name).classList.remove("is-valid");
		document.getElementById(name).classList.remove("is-invalid");
	},
	
	parseDate: () => {
		return Date.UTC(
				document.getElementById("birthDateYearField").value,
				document.getElementById("birthDateMonthField").value,
				document.getElementById("birthDateDayField").value
		);
	},

	validation: {
		clearAllFields: () => {
			validation.clearField(document.getElementById("birthDateYearField"));
			validation.clearField(document.getElementById("birthDateMonthField"));
			validation.clearField(document.getElementById("birthDateDayField"));
			validation.clearField(document.getElementById("sexField"));
			validation.clearField(document.getElementById("cityField"));
			validation.clearField(document.getElementById("descriptionField"));
		},
		
		validateBirthDate: (updateField) => {
			let date = new Date(profile.parseDate());
			let result = false;
			if (
					(document.getElementById("birthDateYearField").value == date.getUTCFullYear()) &&
					(document.getElementById("birthDateMonthField").value == date.getUTCMonth()) &&
					(document.getElementById("birthDateDayField").value == date.getUTCDate())
			){
				let age = 
					new Date(
							Date.now() -
							profile.parseDate()
					).getYear() - 70;
				result = (age >= 18);
			}
			
			if (updateField === true){
				validation.updateField(document.getElementById("birthDateYearField"), result);
				validation.updateField(document.getElementById("birthDateMonthField"), result);
				validation.updateField(document.getElementById("birthDateDayField"), result);
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
		let date = new Date(profile.parseDate());
		let profileData = {
			day: date.getUTCDate(),
			month: date.getUTCMonth(),
			year: date.getUTCFullYear(),
			description: document.getElementById("descriptionField").value,
			city: document.getElementById("cityField").value,
			sex: document.getElementById("sexField").value,
			premium: document.getElementById("premiumField").checked
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
			
			document.getElementById("birthDateDayField").onkeyup = () => profile.notifyChange("birthDate");
			document.getElementById("birthDateMonthField").onkeyup = () => profile.notifyChange("birthDate");
			document.getElementById("birthDateYearField").onkeyup = () => profile.notifyChange("birthDate");
			document.getElementById("descriptionField").onkeyup = () => profile.notifyChange("description");
			document.getElementById("sexField").onclick = () => profile.notifyChange("sex");
			document.getElementById("cityField").onkeyup = () => profile.notifyChange("city");
			document.getElementById("premiumField").onclick = () => profile.notifyChange("premium");
			
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
					
					window.minRateCriteria = Number(body.rating) + 1;
					document.getElementById("minRatingCriteriaField").max = window.minRateCriteria;
					
					document.getElementById("birthDateDayField").value = profile.UserData.birthDate.getUTCDate();
					document.getElementById("birthDateMonthField").value = profile.UserData.birthDate.getUTCMonth() + 1;
					document.getElementById("birthDateYearField").value = profile.UserData.birthDate.getUTCFullYear();
					document.getElementById("cityField").value = ""+body.city;
					document.getElementById("sexField").value = ""+body.sex;
					document.getElementById("descriptionField").value = ""+body.description;
					document.getElementById("premiumField").checked = body.premium;
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
