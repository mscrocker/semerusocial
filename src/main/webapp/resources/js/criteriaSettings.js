const criteriaSettings = {
	criteria: {
		minAge: null,
		maxAge: null,
		city: null,
		sex: null
	},
	
	baseURL: null,
	
	updateHTML: () => {
		document.getElementById("minAgeCriteriaField").value = criteriaSettings.criteria.minAge;
		document.getElementById("maxAgeCriteriaField").value = criteriaSettings.criteria.maxAge;
		document.getElementById("cityCriteriaField").value = criteriaSettings.criteria.city;
		document.getElementById("sexCriteriaField").value = criteriaSettings.criteria.sex;
	},
	
	init: (baseURL) => {
		criteriaSettings.baseURL = baseURL;
		document.getElementById("minAgeCriteriaField").onchange = criteriaSettings.notifyChange;
		document.getElementById("maxAgeCriteriaField").onchange = criteriaSettings.notifyChange;
		document.getElementById("sexCriteriaField").onchange = criteriaSettings.notifyChange;
		document.getElementById("cityCriteriaField").onchange = criteriaSettings.notifyChange;
		
		let url = baseURL + "backend/criteria";
		let params = {
			METHOD: 'GET'
		};
		user.authFetch(url, params, (response) => {
			if (response.status === 200){
				response.json().then((body) => {
					criteriaSettings.criteria = body;
					criteriaSettings.updateHTML();
				}).catch((error) => {
					
				});
			}
			
		}, (error) => {
			
		});
	},
	
	notifyChange: () => {
		document.getElementById("criteriaButton").disabled = false;
	},
	
	clearFeedback: (name) => {
		document.getElementById(name).classList.remove("is-valid");
		document.getElementById(name).classList.remove("is-invalid");
	},
	
	validateFields: () => {
		let minAge = document.getElementById("minAgeCriteriaField").value;
		let maxAge = document.getElementById("maxAgeCriteriaField").value;
		let sex = document.getElementById("sexCriteriaField").value;
		let city = document.getElementById("cityCriteriaField").value;
		
		criteriaSettings.clearFeedback("minAgeCriteriaField");
		criteriaSettings.clearFeedback("maxAgeCriteriaField");
		criteriaSettings.clearFeedback("cityCriteriaField");
		criteriaSettings.clearFeedback("sexCriteriaField");
		
		if (minAge > maxAge){
			document.getElementById("minAgeCriteriaField").classList.add("is-invalid");
			document.getElementById("maxAgeCriteriaField").classList.add("is-invalid");
			document.getElementById("minAgeCriteriaFieldFeedback").innerText = "Minimum age can not be higher than maximum age.";
			document.getElementById("maxAgeCriteriaFieldFeedback").innerText = "Minimum age can not be higher than maximum age.";
			return false;
		}
		
		document.getElementById("minAgeCriteriaField").classList.add("is-valid");
		document.getElementById("maxAgeCriteriaField").classList.add("is-valid");
		document.getElementById("cityCriteriaField").classList.add("is-valid");
		document.getElementById("sexCriteriaField").classList.add("is-valid");
		return true;
	},
	
	updateCriteria: () => {
		document.getElementById("criteriaButton").disabled = true;
		if (criteriaSettings.validateFields()){
			
			let criteria = {
				minAge: document.getElementById("minAgeCriteriaField").value,
				maxAge: document.getElementById("maxAgeCriteriaField").value,
				city: document.getElementById("cityCriteriaField").value,
				sex: document.getElementById("sexCriteriaField").value
			};
			
			let url = criteriaSettings.baseURL + "backend/users/criteria";
			let params = {
				method: 'PUT',
				body: criteria
			};
			user.authFetch(url, params, (response) => {
				
			}, (errors) => {
				
			});
		}
	}
};