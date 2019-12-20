const handleRegister = (e, params, baseUrl) => {
	e.preventDefault();
	customAlert.hideAlert();

	const url = baseUrl + "users/signUp";
	const date = new Date(params.date);
	const userData = {
			userName: params.username,
			password: params.password,
			day: date.getDate(),
			month: date.getMonth() + 1, // from 0 -> 11 and we want
						    // 1 -> 12
			year:date.getFullYear(),
			sex: params.genre,
			city: params.city,
			description: params.description
		};
	const fetchParams = {
			method: 'POST',
			body: JSON.stringify(userData),
			headers: { "Content-Type": "application/json" }
	};

	fetch(url, fetchParams).then((response) => {
		if (response.status !== 201){
			customAlert.showAlertFromResponse(response);
		}
		else {
		response.json().then((body) => {
			
		    localStorage.setItem('user_jwt', body.jwt);
		    localStorage.setItem('user_name', body.userName);
			
			window.location.href =  new URL("../semerusocial/login", document.documentURI).href;
		});
	}
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};