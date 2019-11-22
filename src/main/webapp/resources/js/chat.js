const chat = {
	conversation: null,
	form: null,
	baseURL: null,
	scrollableDiv: null,
	buttonCollapse: null,
	friendScroll: null,
	loadPrevious: true,
	loadMoreFriends: false,
	friendList: [],
	chatMessage: {},
	activeFriend: 0,
	currentMessagePage: 0,
	friendListPage: 0,
	init: (baseURL) => {
		chat.baseURL = baseURL;
		$(window)
			.resize(function () {
				if (document.documentElement.clientWidth <= 761) {
					if (!$('#sidebar, #content')
						.hasClass("active")) {
						chat.buttonPressed();
					}
				}
			});
		chat.buttonCollapse = document.getElementById("backArrow");
		$('#sidebarCollapse')
			.on('click', chat.buttonPressed);

		chat.conversation = document.querySelector('.conversation-container');
		chat.friendList = document.getElementById("friendHolder");
		chat.form = document.querySelector('.conversation-compose');
		chat.form.addEventListener('submit', messages.newMessage);
		chat.scrollableDiv = document.getElementById("content");
		chat.scrollableDiv.addEventListener('scroll', () => {
			// We need to fetch previous message
			if (chat.loadPrevious && chat.scrollableDiv.scrollTop === 0) {
				console.log("chat scroll");
				apis.fetchFriendMessages(chat.activeFriend);
			}
		});
		apis.fetchFriendMessages(0);
	},
	buttonPressed: () => {
		chat.buttonCollapse.classList.toggle("fa-chevron-circle-left");
		chat.buttonCollapse.classList.toggle("fa-chevron-circle-right");
		$('#sidebar, #content')
			.toggleClass('active');
		$('.collapse.in')
			.toggleClass('in');
		$('a[aria-expanded=true]')
			.attr('aria-expanded', 'false');
	}
};

let friends = {
	switchToUser: (friendId) => {

		chat.currentMessagePage = 0;
		let map = chat.chatMessage[chat.activeFriend];
		map.scroll = chat.conversation.scrollHeight;
		while (chat.conversation.lastChild) {
			chat.conversation.lastChild.remove();
		}
		chat.activeFriend = friendId;
		messages.loadExistingMessages(map.messages);
		apis.mockUpChats();
	},

}
let messages = {
	loadExistingMessages: (messageList) => {
		let documentFragment = document.createDocumentFragment();
		messageList.forEach(mess => {
			documentFragment.appendChild(mess);
		});
		chat.conversation.appendChild(documentFragment);

	},
	addMessage: (message) => {

		var built_message = messages.buildMessage(message);
		chat.conversation.insertAdjacentElement('afterbegin', built_message);
		let list = chat.chatMessage[chat.activeFriend];
		if (!list) {
			list = chat.chatMessage[chat.activeFriend] = { messages: [], scroll: 0 };
		}
		list.messages.unshift(built_message);
		messages.animateMessage(built_message);
		chat.scrollableDiv.scrollTop = 0;
	},
	animateMessage: (message) => {
		setTimeout(() => {
			var tick = message.querySelector('.tick');
			tick.classList.remove('tick-animation');
		}, 500);

	},
	buildMessage: ({
		text,
		time,
		sendByMe
	}) => {
		var element = document.createElement('div');

		(sendByMe === true) ? element.classList.add('message', 'sent'): element.classList.add('message',
			'received');

		element.innerHTML = text +
			'<span class="metadata">' +
			'<span class="time">' + time + '</span>' +
			'<span class="tick tick-animation">' +
			'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck" x="2047" y="2061"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#92a58c"/></svg>' +
			'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck-ack" x="2063" y="2076"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#4fc3f7"/></svg>' +
			'</span>' +
			'</span>';
		return element;
	},
	newMessage: (e) => {
		var input = e.target.input;
		if (input.value) {
			var message = messages.buildMessage({
				text: input.value,
				time: moment()
					.format('h:mm A'),
				sendByMe: true
			});
			chat.conversation.appendChild(message);
			chat.scrollableDiv.scrollTop = chat.scrollableDiv.scrollHeight;
		}

		input.value = '';
		e.preventDefault();
	}
};
let apis = {
	addFriendToList: (friend) => {
		let li = document.createElement('li');

		let a = document.createElement('a');
		let imgSpan = document.createElement('img');
		let titleSpan = document.createElement('span');
		let message = document.createElement('p');
		message.innerText = friend.lastMessage;
		titleSpan.innerText = friend.name;
		li.appendChild(a);
		a.appendChild(imgSpan);
		a.appendChild(titleSpan);
		a.appendChild(message);

		li.setAttribute('id', friend.id);
		a.setAttribute('class', 'friend');
		a.setAttribute('data-toggle', 'collapse');
		a.setAttribute('aria-expanded', 'false');

		a.setAttribute('href', '#');
		a.onclick = () => {
			if (friend.id != chat.activeFriend) {
				console.log(friend.id);
				friends.switchToUser(friend.id);
			}
		};

		imgSpan.setAttribute('class', 'user-img');
		titleSpan.setAttribute('class', 'user-title');

		message.setAttribute('class', 'user-desc');
		chat.friendList.appendChild(li);
		chat.chatMessage['friend.id'] = {
			messages: [],
			scroll: 0
		};

	},
	mockUpFriends: () => {
		for (i = 0; i < 4; i++) {
			friends.addFriendToList({
				id: 1,
				lastMessage: "x",
				name: i
			});
		}
	},
	mockUpChats: () => {
		for (i = 0; i < 4; i++) {
			let rand = Math.random()
				.toString(36)
				.replace(/[^a-z]+/g, '')
				.substr(0, 5);
			messages.addMessage({
				text: rand,
				time: moment()
					.format('h:mm A'),
				sendByMe: i % 2
			});
		}
	},

	fetchFriendMessages: (friendId) => {

		/*
		 * user.authFetch(chat.baseUrl + "conversationMessage", {
		 * method: 'GET' }, (response) => { if (response.status == 400) {
		 * console.log("error"); } else if (response.status !== 200) {
		 * console.log("error"); } else { response.json().then((body) => { //
		 * if (body.hasMore){ chat.currentMessagePage++;
		 * chat.loadPrevious = true; }
		 * 
		 * body.items.forEach(message => { let responseMessage =
		 * chat.buildMessage(responseMessage .text,responseMessage
		 * .time);
		 * chat.conversation.insertAdjacentElement('afterbegin',responseMessage ); //
		 * chat.animateMessage(message); }); let list =
		 * chat.chatMessage['${friend.id}'];
		 * list.messages.push(body.items);
		 * 
		 * }); }
		 * 
		 * });
		 */
		apis.mockUpChats();
	},
	loadFriendList: () => {
		if (chat.loadMoreFriends) {
			/*
			 * user.authFetch(chat.baseUrl + "getFriends", { method:
			 * 'GET' }, (response) => { if (response.status == 400) {
			 * console.log("error"); } else if (response.status !==
			 * 200) { console.log("error"); } else {
			 * response.json().then((body) => { // if
			 * (body.hasMore){ chat.friendListPage++;
			 * chat.loadPrevious = true; }
			 * 
			 * body.items.forEach(friend => {
			 * chat.addFriendToList(friend); });
			 * 
			 * }); } });
			 */
			chat.mockUpFriends();
		}
	}
};
