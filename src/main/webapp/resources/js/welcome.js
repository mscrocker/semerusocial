
let init = (baseURL) => {

	
	if (user.checkLoggedIn()){
		window.location.href = baseURL + "findFriend";
	} else {
		window.location.href = baseURL + "login";
	}
	
};