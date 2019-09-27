const updateLoggedIn = (loginButtonName, userURL) => {
	if ((sessionStorage.user_jwt === undefined) || (sessionStorage.user_name === undefined)){
		return;
	}
	document.getElementById(loginButtonName).innerText = sessionStorage.user_name;
	document.getElementById(loginButtonName).formAction = userURL;
};

const authFetch = (url, params, onSuccess, onErrors) => {
	if (sessionStorage.user_jwt !== undefined){
		params.headers.Authorization = 'Bearer ' + sessionsStorage.user_jwt;
	}
	fetch(url, params)
	.then((response) => onSuccess(response))
	.catch((errors) => onErrors(errors));
};