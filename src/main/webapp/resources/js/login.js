const handleLogin = (e, params, baseUrl) => {
	e.preventDefault();
	
	const url = baseUrl + "users/login";
	const userData = {
		userName: params.username,
		password: params.password
	};
	const fetchParams = {
			method: 'POST',
			body: JSON.stringify(userData),
			headers: { "Content-Type": "application/json" }
	};

	fetch(url, fetchParams).then((response) => {
		if (response.status !== 200){
			console.log("ERROR LOGGING IN!");
		}
		
		response.json().then((body) => {
			sessionStorage.setItem('user_jwt', body.jwt);
			sessionStorage.setItem('user_name', body.userName);
			
			window.history.back();
			
		});
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};