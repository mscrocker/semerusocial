const friendList = {
	getPage: () => {
		let url = new URL(window.location.href);
		let page = url.searchParams.get("page");
		return ((page === null) || (page === undefined)) ? 0 : page;
	},
	
	generateHTML: (friendData, baseURL) => {
		let lines = [
		`	<tr>`,
		`		<td>${friendData.userName}</td>`,
		`		<td>${friendData.age}</td>`,
		`		<td>${friendData.sex}</td>`,
		`		<td>${friendData.city}</td>`,
		`		<td id='rating-${friendData.id}'`,
		`			onmouseout="ratingHandler.onMouseOutHandler(${(friendData.myRating >= 1) ? friendData.myRating : 0}, 'rating-${friendData.id}')">`,
		`			<span onmousemove="ratingHandler.onMouseMoveHandler(0, 'rating-${friendData.id}')"`,
		`			onclick="ratingHandler.onMouseClickHandler(0, () => friendList.rateUser(1, ${friendData.id}, '${baseURL}'), 'rating-${friendData.id}')"`,
		`			class="text-success glyphicon ${(friendData.myRating >= 1) ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>`,
		`			<span onmousemove="ratingHandler.onMouseMoveHandler(1, 'rating-${friendData.id}')"`,
		`			onclick="ratingHandler.onMouseClickHandler(1, () => friendList.rateUser(2, ${friendData.id}, '${baseURL}'), 'rating-${friendData.id}')"`,
		`			class="text-success glyphicon ${(friendData.myRating >= 2) ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>`,
		`			<span onmousemove="ratingHandler.onMouseMoveHandler(2, 'rating-${friendData.id}')"`,
		`			onclick="ratingHandler.onMouseClickHandler(2, () => friendList.rateUser(3, ${friendData.id}, '${baseURL}'), 'rating-${friendData.id}')"`,
		`			class="text-success glyphicon ${(friendData.myRating >= 3) ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>`,
		`			<span onmousemove="ratingHandler.onMouseMoveHandler(3, 'rating-${friendData.id}')"`,
		`			onclick="ratingHandler.onMouseClickHandler(3, () => friendList.rateUser(4, ${friendData.id}, '${baseURL}'), 'rating-${friendData.id}')"`,
		`			class="text-success glyphicon ${(friendData.myRating >= 4) ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>`,
		`			<span onmousemove="ratingHandler.onMouseMoveHandler(4, 'rating-${friendData.id}')"`,
		`			onclick="ratingHandler.onMouseClickHandler(4, () => friendList.rateUser(5, ${friendData.id}, '${baseURL}'), 'rating-${friendData.id}')"`,
		`			class="text-success glyphicon ${(friendData.myRating == 5) ? 'glyphicon-star' : 'glyphicon-star-empty'}"></span>`,
		`		</td>`,
		`		<td>`,
		`			<button class="btn btn-primary ml-0 mr-3" onclick="window.location.href='${baseURL}chat/${friendData.id}'">Chat</button>`,
		`			<button class="btn btn-danger ml-3 mr-0" onclick="friendList.blockFriend(${friendData.id},'${baseURL}')">Block</button>`,
		`		</td>`,
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
				customAlert.hideAlert();
				if (page > 0){
					friendList.enableButton("previousButton", baseURL, page);
				}
				if (body.existMoreElements === true){
					friendList.enableButton("nextButton", baseURL, page);
				}
				
				let friends = body.elements;
				let aux = "";
				for (let i = 0; i < friends.length; i++){
					aux += friendList.generateHTML(friends[i], baseURL);
				}
				friendList.insertHTML(aux);
				
				
				
			}).catch((errors) => {
				customAlert.showAlert(errors);
			});
		}, (errors) => {
			customAlert.showAlertFromResponse(response);
		});
	},
	
	
	blockFriend: function (friendId, baseURL) {
		let url = baseURL + "backend/friends/block";
		let params = {
			method: 'POST',
			body: JSON.stringify({
				id: friendId
			}),
			headers: { "Content-Type": "application/json" }
		};
		user.authFetch(url, params, (response) => {
			if (response.status === 204){
				window.location.reload(false);
			}
		}, (errors) => {
			customAlert.showAlert(errors);
		});
	},
	
	
	
	rateUser: (rating, userId, baseURL) => {
		let url = baseURL + "backend/users/rate";
		let params = {
			method: 'POST',
			body: JSON.stringify({
				rate: rating,
				userObject: userId
			}),
			headers: { "Content-Type": "application/json" }
		};
		user.authFetch(url, params, (response) => {
			response.json().then((body) => {
				customAlert.hideAlert();
			}).catch((errors) => {
				customAlert.showAlertFromResponse(response);
			});
		}, (errors) => {
			customAlert.showAlert(errors);
		});
		return true;
	}
};

let ratingHandler = {
	
	setRateDisplay: (value, container) => {
		container = document.getElementById(container);
		for (let i = 0; i < 5; i++){
			if (i < value){
				container.children[i].classList.remove("glyphicon-star-empty");
				container.children[i].classList.add("glyphicon-star");
			} else {
				container.children[i].classList.add("glyphicon-star-empty");
				container.children[i].classList.remove("glyphicon-star");
			}
			
		}
	},
	
	onMouseMoveHandler: function (starNumber, container, valueValidator) {
		if ((valueValidator === undefined) || (valueValidator(starNumber) === true)){
			this.setRateDisplay(starNumber + 1, container);
		}
	},
	
	onMouseOverHandler: function (originalRating, container, valueValidator) {
		if ((valueValidator === undefined) || (valueValidator(starNumber) === true)){
			this.setRateDisplay(originalRating, container);
		}
	},
	
	onMouseOutHandler: function (originalRating, container) {
		this.setRateDisplay(originalRating, container);
	},
	
	onMouseClickHandler: function (starNumber, callback, container, valueValidator) {
		if ((valueValidator === undefined) || (valueValidator(starNumber) === true)){
			if (callback()){
				this.setRateDisplay(starNumber - 1, container);
				let containerDiv = document.getElementById(container);
				containerDiv.onmouseout = () => ratingHandler.onMouseOutHandler(starNumber + 1, container);
			}
		}
	}
};