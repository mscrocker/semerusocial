const user = {
	updateLoggedIn: (loginButtonName, userURL) => {
		if ((localStorage.user_jwt === undefined) || (localStorage.user_name === undefined)){
			return;
		}
		document.getElementById(loginButtonName).innerText = localStorage.user_name;
		document.getElementById(loginButtonName).formAction = userURL;
	},

	authFetch: (url, params, onSuccess, onErrors) => {
		if (localStorage.user_jwt !== undefined){
			if (params.headers === undefined){
				params.headers = {};
			}
			
			params.headers.Authorization = 'Bearer ' + localStorage.user_jwt;
		}
		fetch(url, params)
		.then((response) => onSuccess(response))
		.catch((errors) => onErrors(errors));
	},

	checkLoggedIn: () => {
		return localStorage.user_jwt !== undefined;
	},

	logOut: () => {
		delete localStorage.user_jwt;
		delete localStorage.user_name;
	}
};

