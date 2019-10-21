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
					profile.UserData.description = body.description;
					
					document.getElementById("ageField").value = ""+body.age;
					document.getElementById("cityField").value = ""+body.city;
					document.getElementById("sexField").value = ""+body.sex;
					document.getElementById("descriptionField").value = ""+body.description;
				}
				);
			});
		}else {
			window.location.href = baseURL + "login";
		}
	}

	
};
