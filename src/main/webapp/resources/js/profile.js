const profile = {

	UserData: {
		'age' : null,
		'sex' : null, 
		'city' : null
	},
	
	notifyChange: () => {
		document.getElementById("profileButton").disabled = false;
	},
	
	clearFeedback: (name) => {
		document.getElementById(name).classList.remove("is-valid");
		document.getElementById(name).classList.remove("is-invalid");
	},
	

	validateFields: () => {
		let age = document.getElementById("ageField").value;
		let sex = document.getElementById("sexField").value;
		let city = document.getElementById("cityField").value;
		let description = document.getElementById("descriptionField").value;
		
		
		profile.clearFeedback("ageField");
		profile.clearFeedback("descriptionField");
		profile.clearFeedback("cityField");
		profile.clearFeedback("sexField");
		
		
		document.getElementById("ageField").classList.add("is-valid");
		document.getElementById("descriptionField").classList.add("is-valid");
		document.getElementById("cityField").classList.add("is-valid");
		document.getElementById("sexField").classList.add("is-valid");
		return true;
	},
	

	updateCriteria: () => {
		document.getElementById("profileButton").disabled = true;
		if (profile.validateFields()){
			//TODO: undo mocked date
			let date = new Date(Date.now());
			let profile = {
				//age: document.getElementById("ageField").value,
				day: date.getUTCDay(),
				month: date.getUTCMonth(),
				year: date.getUTCFullYear() - document.getElementById("ageField").value,
					
				
				description: document.getElementById("descriptionField").value,
				city: document.getElementById("cityField").value,
				sex: document.getElementById("sexField").value
			};
			
			let url = criteriaSettings.baseURL + "backend/users/updateProfile";
			let params = {
				method: 'PUT',
				body: JSON.stringify(profile),
				headers: { "Content-Type": "application/json" }
			};
			user.authFetch(url, params, (response) => {
				if (response.status !== 204){
					showAlert("Internal server error");
				}
			}, (errors) => {
				showAlert("Internal server error");
			});
		}
	},

	initProfile: (baseURL) => {
		const url = baseURL + "backend/users/data" ;
		if (user.checkLoggedIn()){
			
			document.getElementById("ageField").onclick = profile.notifyChange;
			document.getElementById("descriptionField").onclick = profile.notifyChange;
			document.getElementById("sexField").onclick = profile.notifyChange;
			document.getElementById("cityField").onclick = profile.notifyChange;
			
			document.getElementById("profileButton").onclick = profile.updateCriteria;
			
			user.authFetch(url, {method: 'GET'}, (response) => {	
				if (response.status !== 200){
					console.log("ERROR!");
					return;
				}
				response.json().then((body) => {
					profile.UserData.age = body.age;
					profile.UserData.sex = body.sex;
					profile.UserData.city = body.city;
					profile.UserData.description = body.description;
					
					document.getElementById("ageField").value = ""+body.age;
					document.getElementById("cityField").value = ""+body.city;
					document.getElementById("sexField").value = ""+body.sex;
					document.getElementById("descriptionField").value = ""+body.description;
				}
				);
			});
		}else {
			window.location.href = baseURL + "login";
		}
	}

	
};
