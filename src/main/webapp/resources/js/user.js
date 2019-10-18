const user = {
	updateLoggedIn: (loginButtonName, userURL) => {
		if ((sessionStorage.user_jwt === undefined) || (sessionStorage.user_name === undefined)){
			return;
		}
		document.getElementById(loginButtonName).innerText = sessionStorage.user_name;
		document.getElementById(loginButtonName).formAction = userURL;
	},

	authFetch: (url, params, onSuccess, onErrors) => {
		if (sessionStorage.user_jwt !== undefined){
			if (params.headers === undefined){
				params.headers = {};
			}
			
			params.headers.Authorization = 'Bearer ' + sessionStorage.user_jwt;
		}
		fetch(url, params)
		.then((response) => onSuccess(response))
		.catch((errors) => onErrors(errors));
	},

	checkLoggedIn: () => {
		return sessionStorage.user_jwt !== undefined;
	},

	logOut: () => {
		delete sessionStorage.user_jwt;
		delete sessionStorage.user_name;
	}
};

