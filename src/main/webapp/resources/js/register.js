const handleRegister = (e, params, baseUrl) => {
	e.preventDefault();
	console.log("yes");
	const url = baseUrl + "users/signUp";
	const date = new Date(params.date);
	const userData = {
			userName: params.username,
			password: params.password,
			day: date.getDate(),
			month: date.getMonth() + 1, // from 0 -> 11 and we want 1 -> 12
			year:date.getFullYear(),
			sex: params.genre,
			city: params.city
		};
	const fetchParams = {
			method: 'POST',
			body: JSON.stringify(userData),
			headers: { "Content-Type": "application/json" }
	};

	fetch(url, fetchParams).then((response) => {
		if (response.status !== 201){
			console.log("ERROR ON REGISTER PROCEDURE IN!");
			showAlert(response);
		}
		else {
		response.json().then((body) => {
			sessionStorage.setItem('user_jwt', body.jwt);
			sessionStorage.setItem('user_name', body.userName);
			
			window.history.back();
			
		});
	}
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};