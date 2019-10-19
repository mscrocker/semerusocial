let UserData = {
		'age' : null,
		'sex' : null, 
		'city' : null,
		'description' : null
};

const initProfile = (baseURL) => {
	const url = baseURL + "backend/users/data" ;
	if (checkLoggedIn()){
		authFetch(url, {method: 'GET'}, (response) => {	
			if (response.status !== 200){
				console.log("ERROR!");
				return;
			}
			response.json().then((body) => {
				UserData.age = body.age;
				UserData.sex = body.sex;
				UserData.city = body.city;
				UserData.description = body.description;
				
				document.getElementById("ageField").innerText = " "+body.age;
				document.getElementById("cityField").innerText = " "+body.city;
				document.getElementById("sexField").innerText = " "+body.sex;
				document.getElementById("descriptionField").innerText = " "+body.description;
			}
			);
		});
	}else {
		window.location.href = baseURL + "login";
	}
};

