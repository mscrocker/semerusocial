let UserData = {
		'age' : null,
		'sex' : null, 
		'city' : null
};

const initProfile = (baseURL) => {
	const url = baseURL + "users/data" ;
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
				
				document.getElementById("ageField").innerText = " "+body.age;
				document.getElementById("cityField").innerText = " "+body.city;
				document.getElementById("sexField").innerText = " "+body.sex;
			}
			);
		});
	}else {
		window.location.href = baseURL + "login";
	}
};

