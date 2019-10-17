const handleLogin = (e,params, baseUrl) => {
	e.preventDefault();
	showAlert(null);
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
		
		response.json().then((body) => {
			if (response.status !== 200){
				showAlert("Error: unable to log in with given credentials");
				return;
			}
			
			sessionStorage.setItem('user_jwt', body.jwt);
			sessionStorage.setItem('user_name', body.userName);
			
			
			window.location.href = baseUrl;
			
		}).catch((e) => {
			console.log("Error logging in!");
		});
		
		
	}).catch((error) => {
		console.log("ERROR: " + error);
	});
	
};