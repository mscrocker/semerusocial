const friendList = {
	getPage: () => {
		let url = new URL(window.location.href);
		let page = url.searchParams.get("page");
		return ((page === null) || (page === undefined)) ? 0 : page;
	},
	
	generateHTML: (friendData) => {
		let lines = [
		`	<tr>`,
		`		<td>${friendData.userName}</td>`,
		`		<td>${friendData.age}</td>`,
		`		<td>${friendData.sex}</td>`,
		`		<td>${friendData.city}</td>`,
		`	</tr>`
		];
		return lines.join("\n");
	},
	
	enableButton: (name, baseURL, currentPage) => {
		let button = document.getElementById(name);
		let destination = (name === "nextButton") ? currentPage + 1 : 
							((name === "previousButton") ? currentPage - 1 : null);
		button.addEventListener("click", () => {
			window.location.href = baseURL + "users/friends?page=" + destination;
		});
		button.classList.add("btn-primary");
	},
	
	insertHTML: (html) => {
		document.getElementById("friendsTable").innerHTML = html;
	},
	
	init: (baseURL) => {
		let page = friendList.getPage();
		
		let url = baseURL + "/backend/friends/friendList?page=" + page;
		let params = {
			METHOD: 'GET'
		};
		user.authFetch(url, params, (response) => {
			response.json().then((body) => {
				showAlert(null);
				if (page > 0){
					friendList.enableButton("previousButton", baseURL, page);
				}
				if (body.existMoreFriends === true){
					friendList.enableButton("nextButton", baseURL, page);
				}
				
				let friends = body.friends;
				let aux = "";
				for (let i = 0; i < friends.length; i++){
					aux += friendList.generateHTML(friends[i]);
				}
				friendList.insertHTML(aux);
				
				
				
			}).catch((errors) => {
				showAlert("Error getting friend list");
				console.log("ERROR: " + errors);
			});
		}, (erros) => {
			showAlert("Error getting friend list");
			console.log("ERROR: " + errors);
		});
	}
};