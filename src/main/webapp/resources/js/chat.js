/*jshint esversion: 6 */

const chat = {
	conversation: null, // element where messages are visible
	baseURL: null, // To make petitions to backend
	scrollableDiv: null, // Scroll elemennt on chat
	buttonCollapse: null, // Button to collapse sidebar
	friendScroll: null, // Scroll element on friendList
	loadPrevious: true, // Whether we know there are previous messages or
	// not
	loadMoreFriends: true, // Whether we know there are more friends or not
	friendList: [], // List of friends
	chatMessage: {}, // Chat message
	// {messages:[],scroll:x,lastActivePage:y,needMoreLoading:true/false}
	activeFriend: 0, // Id of active friend
	currentMessagePage: 0, // Page of current fetched chat
	friendListPage: 0, // Page of current fetched friends

	// Called on paged load;
	init: (baseURL) => {
		// Module variables initialization
		chat.baseURL = baseURL;
		chat.buttonCollapse = document.getElementById("backArrow");
		chat.conversation = document.querySelector('.conversation-container');
		chat.friendList = document.getElementById("friendHolder");
		chat.scrollableDiv = document.getElementById("content");
		// When we get to the top of the chat scroll we fetch previous
		// pages if needed
		chat.scrollableDiv.addEventListener('scroll', () => {
			// We need to fetch previous message
			if (chat.loadPrevious && chat.scrollableDiv.scrollTop === 0) {
				console.log("chat scroll");
				apis.fetchFriendMessages(chat.activeFriend);
			}
		});

		// Automatically collapses the sidebar on resize;
		$(window).resize(function () {
			if (document.documentElement.clientWidth <= 761) {
				if (!$('#sidebar, #content')
					.hasClass("active")) {
					chat.buttonPressed();
				}
			}
		});
		$('#sidebarCollapse')
			.on('click', chat.buttonPressed);

		// Handler for creating your messages
		document.querySelector('.conversation-compose').addEventListener('submit', messages.newMessage);

		// init page
		chat.initScreen();

	},
	// Collapse Sidebar Function
	buttonPressed: () => {
		chat.buttonCollapse.classList.toggle("fa-chevron-circle-left");
		chat.buttonCollapse.classList.toggle("fa-chevron-circle-right");
		$('#sidebar, #content')
			.toggleClass('active');
		$('.collapse.in')
			.toggleClass('in');
		$('a[aria-expanded=true]')
			.attr('aria-expanded', 'false');
	},
	initScreen: () => {

		apis.loadFriendList();
		let id = window.location.pathname.split("/").pop();
		if (!isNaN(parseFloat(id)) && !isNaN(id - 0)) {
			chat.activeFriend = id;
		} else {
			chat.activeFriend = chat.friendList.firstElementChild.getAttribute("data-id");
		}

		chat.chatMessage[chat.activeFriend] = {
			messages: [],
			scroll: 0,
			needMoreLoading: true,
			lastActivePage: 0
		};
		apis.fetchFriendMessages(chat.activeFriend);
	},
	formatDate: (date) => {
		let now = moment();
		if (date.isSame(now, "day"))
			return date.format("HH:mm");

		now = now.startOf("day").add(-1, "day");
		if (date.isSame(now, "day"))
			return "Yesterday";
		now = now.add(-6, "day");
		if (date.isAfter(now))
			return date.format("dddd");

		return date.format("L");
	}
};

let friends = {
	addFriendToList: ({
		lastMessage,
		name,
		id,
		count,
		date
	}) => {
		// Friend already on list
		if (document.querySelector(`li[data-id="${id}"]`)) {
			return;
		}
		let li = document.createElement('li');
		let a = document.createElement('div');
		let imgSpan = document.createElement('span');
		let lineContainer = document.createElement('div');

		let div1 = document.createElement('div');
		let div2 = document.createElement('div');

		let titleSpan = document.createElement('div');
		let userDate = document.createElement('div');
		let notis = document.createElement('span');
		let message = document.createElement('p');

		message.innerText = lastMessage;

		if (count && count !== 0) {
			notis.innerText = count;

		} else {
			notis.classList.add("hidden");
		}


		titleSpan.innerText = name;
		userDate.innerText = chat.formatDate(date);


		li.appendChild(a);
		a.appendChild(imgSpan);
		div1.appendChild(titleSpan);
		div1.appendChild(userDate);

		div2.appendChild(message);
		div2.appendChild(notis);
		lineContainer.appendChild(div1);
		lineContainer.appendChild(div2);
		a.appendChild(lineContainer);


		// li.setAttribute('data-userid', friend.id);
		a.setAttribute('class', 'friend');
		a.setAttribute('data-toggle', 'collapse');
		a.setAttribute('aria-expanded', 'false');

		a.onclick = () => {
			if (id != chat.activeFriend) {
				console.log(id);
				document.querySelector(`li[data-id="${id}"] .line2 .notis`).classList.add("hidden");
				document.querySelector(`[data-id="${id}"]`).firstElementChild.classList.add("clicked");
				document.querySelector(`[data-id="${chat.activeFriend}"]`).firstElementChild.classList.remove("clicked");
				friends.switchToUser(id);


			}
		};

		lineContainer.setAttribute('class', "user-wrapper");
		div1.setAttribute('class', "line1");
		div2.setAttribute('class', "line2");
		userDate.setAttribute('class', "user-date");
		notis.setAttribute('class', "notis user-circle");

		imgSpan.setAttribute('class', 'user-img');
		titleSpan.setAttribute('class', 'user-title');
		li.setAttribute('data-id', id);
		li.setAttribute('data-date', date.toISOString());


		message.setAttribute('class', 'user-desc');
		let liCount = chat.friendList.childElementCount;
		for (i = 0; i < liCount; i++) {
			let elemDate = chat.friendList.children[i].getAttribute("data-date");
			if (date.toISOString() > elemDate) {
				chat.friendList.insertBefore(li, chat.friendList.children[i]);
				break;
			}
		}
		if (liCount === chat.friendList.childElementCount) {
			chat.friendList.appendChild(li);
		}

	},
	// change the active user for another one;
	switchToUser: (friendId) => {

		// We store the current user info in case we come back to him
		let map = chat.chatMessage[chat.activeFriend];
		map.lastActivePage = chat.currentMessagePage;
		map.needMoreLoading = chat.loadPrevious;
		map.scroll = chat.scrollableDiv.scrollTop;

		// Cleans chat screen
		chat.currentMessagePage = 0;
		while (chat.conversation.lastChild) {
			chat.conversation.lastChild.remove();
		}
		// Prepares for switch
		let newMap = chat.chatMessage[friendId];
		chat.activeFriend = friendId;
		// If we dont have information about the user we fetch it;
		if (!newMap) {
			newMap = chat.chatMessage[friendId] = {
				messages: [],
				scroll: 0,
				needMoreLoading: false,
				lastActivePage: 0
			};
			apis.mockUpChats();
		} else {
			messages.loadExistingMessages(newMap.messages);
		}
		chat.curretMessagePage = newMap.lastActivePage;
		chat.loadPrevious = newMap.needMoreLoading;
		chat.scrollableDiv.scrollTop = newMap.scroll;
	}
};
let messages = {

	// Adds user existing MEssages to chat Screen
	loadExistingMessages: (messageList) => {
		let documentFragment = document.createDocumentFragment();
		messageList.forEach(mess => {
			documentFragment.appendChild(mess);
		});
		chat.conversation.appendChild(documentFragment);

	},

	// Adds received or sent Message to DOM
	addMessage: (message) => {

		let prevScroll = chat.scrollableDiv.scrollHeight;
		var built_message = messages.buildMessage(message);
		chat.conversation.insertAdjacentElement('afterbegin', built_message);
		let list = chat.chatMessage[chat.activeFriend];
		// if (!list) {
		// list = chat.chatMessage[chat.activeFriend] ={ messages: [],
		// scroll:
		// 0,needMoreLoading: false,lastActivePage:0};
		// }
		list.messages.unshift(built_message);

		messages.animateMessage(built_message);

		chat.scrollableDiv.scrollTop += (chat.scrollableDiv.scrollHeight - prevScroll);
	},


	animateMessage: (message) => {
		setTimeout(() => {
			var tick = message.querySelector('.tick');
			tick.classList.remove('tick-animation');
		}, 500);

	},
	// From message metadata build element
	buildMessage: ({
		text,
		date,
		sendByMe
	}) => {
		var element = document.createElement('div');

		let classname = (sendByMe) ? "sent" : "received";

		element.classList.add('message', classname);
		element.innerHTML = text +
			'<span class="metadata">' +
			'<span class="time">' + date + '</span>' +
			'<span class="tick tick-animation">' +
			'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck" x="2047" y="2061"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#92a58c"/></svg>' +
			'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck-ack" x="2063" y="2076"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#4fc3f7"/></svg>' +
			'</span>' +
			'</span>';
		return element;
	},

	// When you input a new message
	newMessage: (e) => {
		var input = e.target.input;
		if (input.value) {
			var message = messages.buildMessage({
				text: input.value,
				date: moment().format("HH:mm"),
				sendByMe: true
			});
			let list = chat.chatMessage[chat.activeFriend];
			list.messages.push(message);
			chat.conversation.appendChild(message);
			chat.scrollableDiv.scrollTop = chat.scrollableDiv.scrollHeight;
		}
		input.value = '';
		e.preventDefault();
	}
};
let apis = {

	mockUpFriends: () => {
		for (i = 0; i < 4; i++) {
			friends.addFriendToList({
				id: i,
				lastMessage: "x",
				name: i,
				count: Math.floor(Math.random() * 100),
				date: moment()
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
				date: chat.formatDate(moment()),
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
			apis.mockUpFriends();
		}
	}
};