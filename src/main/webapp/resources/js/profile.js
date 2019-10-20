const profile = {

	UserData: {
				'age' : null,
			'sex' : null, 
			'city' : null
	},

	initProfile: (baseURL) => {
		const url = baseURL + "backend/users/data" ;
		if (user.checkLoggedIn()){
			user.authFetch(url, {method: 'GET'}, (response) => {	
				if (response.status !== 200){
					console.log("ERROR!");
					return;
				}
				response.json().then((body) => {
					profile.UserData.age = body.age;
					profile.UserData.sex = body.sex;
					profile.UserData.city = body.city;
					
					document.getElementById("ageField").innerText = " "+body.age;
					document.getElementById("cityField").innerText = " "+body.city;
					document.getElementById("sexField").innerText = " "+body.sex;
				}
				);
			});
		}else {
			window.location.href = baseURL + "login";
		}
	}

	
};
