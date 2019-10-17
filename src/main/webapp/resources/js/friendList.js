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
		let destination = (name === "next") ? currentPage + 1 : 
							((name === "previous") ? currentPage - 1 : null);
		button.addEventListener("click", () => {
			window.location.href = baseURL + "/users/friends?page=" + destination;
		});
		button.classList.add("btn-primary");
	},
	
	insertFriendHTML: (html) => {
		document.getElementById("friendsTable").innerText = html;
	},
	
	init: (baseURL) => {
		let page = friendList.getPage();
		
		let url = baseURL + "/backend/friends?page=" + page;
		let params = {
			METHOD: 'GET'
		};
		authFetch(url, params, (response) => {
			response.json().then((body) => {
				showAlert(null);
				if (page > 0){
					friendList.enableButton("back", baseURL, page);
				}
				if (body.hasNext === true){
					friendList.enableButton("forward", baseURL, page);
				}
				
				let friends = body.data;
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