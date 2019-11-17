const criteriaSettings = {
	criteria: {
		minAge: null,
		maxAge: null,
		city: null,
		sex: null
	},
	
	baseURL: null,
	
	updateHTML: () => {
		document.getElementById("minAgeCriteriaField").value = ""+criteriaSettings.criteria.minAge;
		document.getElementById("maxAgeCriteriaField").value = ""+criteriaSettings.criteria.maxAge;
		criteriaSettings.citiesList.generateTable(criteriaSettings.criteria.city);
		document.getElementById("sexCriteriaField").value = ""+criteriaSettings.criteria.sex;
	},
	
	init: (baseURL) => {
		criteriaSettings.clearStatusIcons();
		criteriaSettings.baseURL = baseURL;
		document.getElementById("addCityButton").onclick = criteriaSettings.citiesList.addElem;
		document.getElementById("minAgeCriteriaField").onchange = () => criteriaSettings.notifyChange("minAge");
		document.getElementById("maxAgeCriteriaField").onchange = () => criteriaSettings.notifyChange("maxAge");
		document.getElementById("sexCriteriaField").onchange = () => criteriaSettings.notifyChange("sex");
		
		document.getElementById("criteriaButton").onclick = criteriaSettings.updateCriteria;
		
		let url = baseURL + "backend/users/searchCriteria";
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
	
	notifyChange: (name) => {
		criteriaSettings.clearStatusIcons();
		let result = false;
		switch (name) {
			case "minAge": 
				result = criteriaSettings.validation.validateMinAge(true);
				break;
			case "maxAge":
				result = criteriaSettings.validation.validateMaxAge(true);
				break;
			case "sex":
				result = criteriaSettings.validation.validateSex(true);
				break;
			case "city":
				result = criteriaSettings.validation.validateCities(true);
				break;
		}

		document.getElementById("criteriaButton").disabled = !result;

		
	},

	
	validation: {
		clearAllFields: () => {
			validation.clearField(document.getElementById("minAgeCriteriaField"));
			validation.clearField(document.getElementById("maxAgeCriteriaField"));
			validation.clearField(document.getElementById("sexCriteriaField"));
			let numCities = criteriaSettings.citiesList.parseTable().length;
			for (let i = 0; i < numCities; i++){
				validation.clearField(document.getElementById(`cityInputField${i}`));
			}
		},
		
		validateMinAge: (updateField) => {
			let minAge = Number(document.getElementById("minAgeCriteriaField").value);
			let maxAge = Number(document.getElementById("maxAgeCriteriaField").value);
			let result = (minAge <= maxAge) && (minAge >= 18);
			if (updateField === true){
				validation.updateField(document.getElementById("minAgeCriteriaField"), result);
			}
			return result;
		},
		
		validateMaxAge: (updateField) => {
			let minAge = Number(document.getElementById("minAgeCriteriaField").value);
			let maxAge = Number(document.getElementById("maxAgeCriteriaField").value);
			let result = (minAge <= maxAge);
			if (updateField === true){
				validation.updateField(document.getElementById("maxAgeCriteriaField"), result);
			}
			return result;
		},
		
		validateSex: (updateField) => {
			let sex = document.getElementById("sexCriteriaField").value;
			let result = ["MALE", "FEMALE", "ANY", "OTHER"].includes(sex);
			if (updateField === true){
				validation.updateField(document.getElementById("sexCriteriaField"), result);
			}
			return result;
		},
		
		validateCities: (updateField) => {
			let cities = criteriaSettings.citiesList.parseTable();
			let result = true;
			for (let i = 0; i < cities.length; i++){
				let tmpResult = cities[i] !== "";
				if (tmpResult === false){
					result = false;
				}
				if (updateField === true){
					validation.updateField(document.getElementById(`cityInputField${i}`), tmpResult);
				}
			}
			
			return result;
		}
	},
	
	citiesList : {
		parseTable: () => {
			let result = [];
			let table1 = document.getElementById("citiesTable1");
			for (let i = 0; i < table1.rows.length; i++){
				let field = table1.rows[i].cells[0].childNodes[1];
				result.push(""+field.value);
			}
			
			return result;
		},
		
		clearTable: () => {
			let rowNum = document.getElementById("citiesTable1").rows.length;
			for (let i = 0; i < rowNum; i++){
				document.getElementById("citiesTable1").deleteRow(0);
			}
			
		},
		
		generateTable: (data) => {
			
			
			if (data.length < 5){
				document.getElementById("addCityButton").disabled = false;
				document.getElementById("addCityButton").classList.remove("btn-disabled");
			} else {
				document.getElementById("addCityButton").disabled = true;
				document.getElementById("addCityButton").classList.add("btn-disabled");
			}
			

			
			if (data.length <= 5){
				// ONE ROW DISPLAY
				let table = document.getElementById("citiesTable1Body");
				for (let i = data.length - 1; i >= 0; i--){
					let row = table.insertRow(0);
					let cell2 = row.insertCell(0);
					let cell1 = row.insertCell(0);
					
					cell1.innerHTML = [
						`	<input id="cityInputField${i}" class="form-control" type="text" maxlength="30" required value="${data[i].	replace(/"/g, '&quot;')}" onchange="criteriaSettings.notifyChange('city')" />`,
						`	<div class="invalid-feedback">`,
						`		<p id="cityInputFieldFeedback${i}"></p>`,
						`	</div>`
					].join("\n");
					cell2.innerHTML = [
						`<button type="button" class="btn btn-danger" onclick="((event) => criteriaSettings.citiesList.removeElem(event, ${i}))(event)">`,
						`	<span class="glyphicon glyphicon-remove"></span>`,
						`</button>`
					].join("\n");
					cell2.classList.add("text-center");
				}
			}
		},
		
		removeElem : (event, number) => {
			event.preventDefault();
			let elems = criteriaSettings.citiesList.parseTable();
			elems.splice(number, 1);
			criteriaSettings.citiesList.clearTable();
			criteriaSettings.citiesList.generateTable(elems);
			criteriaSettings.notifyChange("city");
			
		},
		
		addElem: () => {
			let elems = criteriaSettings.citiesList.parseTable();
			elems.push("");
			criteriaSettings.citiesList.clearTable();
			criteriaSettings.citiesList.generateTable(elems);
			criteriaSettings.notifyChange("city");
		}
	},
	
	
	
	
	clearStatusIcons: () => {
		document.getElementById("criteriaLoading").classList.add("hidden");
		document.getElementById("criteriaFinished").classList.add("hidden");
		document.getElementById("criteriaError").classList.add("hidden");
	},
	

	
	updateCriteria: () => {
		document.getElementById("criteriaButton").disabled = true;

		criteriaSettings.clearStatusIcons();
		document.getElementById("criteriaLoading").classList.remove("hidden");
		
		let criteria = {
			minAge: Number(document.getElementById("minAgeCriteriaField").value),
			maxAge: Number(document.getElementById("maxAgeCriteriaField").value),
			city: criteriaSettings.citiesList.parseTable(),
			sex: document.getElementById("sexCriteriaField").value
		};
		
		let url = criteriaSettings.baseURL + "backend/users/searchCriteria";
		let params = {
			method: 'PUT',
			body: JSON.stringify(criteria),
			headers: { "Content-Type": "application/json" }
		};
		user.authFetch(url, params, (response) => {
			criteriaSettings.validation.clearAllFields();
			if (response.status !== 204){
				customAlert.showAlertFromResponse(response);
				criteriaSettings.clearStatusIcons();
				document.getElementById("criteriaError").classList.remove("hidden");
			} else {
				criteriaSettings.clearStatusIcons();
				document.getElementById("criteriaFinished").classList.remove("hidden");
			}
		}, (errors) => {
			customAlert.showAlert(errors);
		});
	}
};