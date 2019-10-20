const handleRegister = (e, params, baseUrl) => {
	e.preventDefault();
	const url = baseUrl + "backend/users/signUp";
	const date = new Date(params.date);
	const userData = {
			userName: params.username,
			password: params.password,
			day: date.getDate(),
			month: date.getMonth() + 1, // from 0 -> 11 and we want 1 -> 12
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
			response.json().then((body) => {
				showAlert("Error");
			});
		}
		else {
		response.json().then((body) => {
			
			sessionStorage.setItem('user_jwt', body.jwt);
			sessionStorage.setItem('user_name', body.userName);
			
			window.location.href = baseUrl ;
		});
	}
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};