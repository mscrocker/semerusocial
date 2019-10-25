const handleLogin = (e,params, baseUrl) => {
	e.preventDefault();
	customAlert.hideAlert();
	const url = baseUrl + "backend/users/login";
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
			customAlert.showAlertFromResponse(response);
			return;
		}
		response.json().then((body) => {
			
			
		    localStorage.setItem('user_jwt', body.jwt);
		    localStorage.setItem('user_name', body.userName);
			
			
			window.location.href = baseUrl;
			
		}).catch((e) => {
			console.log("Error logging in!");
		});
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};