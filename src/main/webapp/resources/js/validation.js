let validation = {
	setFieldValid: (field) => {
		field.classList.remove("is-invalid");
		field.classList.add("is-valid");
	},
	
	setFieldInvalid: (field) => {
		field.classList.remove("is-valid");
		field.classList.add("is-invalid");
	},
	
	clearField: (field) => {
		field.classList.remove("is-valid");
		field.classList.remove("is-invalid");
	},
	
	
	
	updateField: (field, valid) => {
		if ((valid === null) || (valid === undefined)){
			validation.clearField(field);
		} else if (valid === true){
			validation.setFieldValid(field);
		} else if (valid === false){
			validation.setFieldInvalid(field);
		}
	},
	
	
	
};