const handleRegister = (e, params, baseUrl) => {
	e.preventDefault();
	console.log("yes");
	const url = baseUrl + "users/signUp";
	const userData = {
		userName: params.username,
		password: params.password,
		age: new Date(params.date).getTime(),
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