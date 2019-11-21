const chat = {
	conversation: null,
	form: null,
	baseURL: null,
	scrollableDiv: null,
	friendScroll: null,
	loadPrevious: false,
	loadMoreFriends: true,
	friendList: [],
	chatMessage: {},
	activeFriend: 0,
	currentMessagePage: 0,
	friendListPage: 0,
	init: (baseURL) => {
	    chat.baseURL = baseURL;
	    $("#sidebar").mCustomScrollbar({
			theme : "minimal",
			onTotalScrollOffset: 130,
			onTotalScroll: () => {
			    console.log("Reaching");
			    chat.loadFriendList();
			}  
		    });
 			    
	    chat.conversation = document.querySelector('.conversation-container');
	    chat.friendList = document.getElementById("friendHolder");
	    chat.form = document.querySelector('.conversation-compose');
	    chat.form.addEventListener('submit', chat.newMessage);
	    chat.scrollableDiv = document.getElementById("content");
	    chat.scrollableDiv.addEventListener('scroll', () => {
		// We need to fetch previous message
	
		if (chat.loadPrevious && chat.scrollableDiv.scrollTop === 0) {
			console.log("chat scroll");
		    chat.fetchFriendMessages(chat.activeFriend);
		}
	    });
	    
	},
	newMessage: (e) => {
	    var input = e.target.input;
		if (input.value) {
			var message = chat.buildMessage(input.value);
			chat.conversation.appendChild(message);
			chat.animateMessage(message);
		}

		input.value = '';
		chat.scrollableDiv.scrollTop = chat.scrollableDiv.scrollHeight;
		e.preventDefault();
	},
	buildMessage: (text,time) => {
	    var element = document.createElement('div');
		element.classList.add('message', 'sent');
		element.innerHTML = text +
			'<span class="metadata">' +
				'<span class="time">' + moment().format('h:mm A') + '</span>' +
				'<span class="tick tick-animation">' +
					'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck" x="2047" y="2061"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#92a58c"/></svg>' +
					'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" id="msg-dblcheck-ack" x="2063" y="2076"><path d="M15.01 3.316l-.478-.372a.365.365 0 0 0-.51.063L8.666 9.88a.32.32 0 0 1-.484.032l-.358-.325a.32.32 0 0 0-.484.032l-.378.48a.418.418 0 0 0 .036.54l1.32 1.267a.32.32 0 0 0 .484-.034l6.272-8.048a.366.366 0 0 0-.064-.512zm-4.1 0l-.478-.372a.365.365 0 0 0-.51.063L4.566 9.88a.32.32 0 0 1-.484.032L1.892 7.77a.366.366 0 0 0-.516.005l-.423.433a.364.364 0 0 0 .006.514l3.255 3.185a.32.32 0 0 0 .484-.033l6.272-8.048a.365.365 0 0 0-.063-.51z" fill="#4fc3f7"/></svg>' +
				'</span>' +
			'</span>';
		return element;
	},
	animateMessage: (message) => {
	    setTimeout( () => {
		var tick = message.querySelector('.tick');
		tick.classList.remove('tick-animation');
	}, 500);
		
	},
	loadFriendList: () => {
	    if (chat.loadMoreFriends) {
	    user.authFetch(chat.baseUrl + "getFriends", {
	            method: 'GET'
	        }, (response) => {
	            if (response.status == 400) {
	                console.log("error");
	            }
	            else if (response.status !== 200) {
	        	console.log("error");
	            }
	            else {
	            response.json().then((body) => {
	        	//
	        	if (body.hasMore){
	        	    chat.friendListPage++;
	        	    chat.loadPrevious = true;
	        	}
	        	
	        	body.items.forEach(friend => {
	        	chat.addFriendToList(friend);
	        	});
	            
	            });
	            }
	        });
	    }
	},
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
	    
	    li.setAttribute('id',friend.id);
            a.setAttribute('class','friend');
            
            a.setAttribute('href','#');
            imgSpan.setAttribute('class','user-img');
            titleSpan.setAttribute('class','user-title');
            message.setAttribute('class','user-desc');
            chat.friendList.appendChild(li);
            chat.chatMessage['${friend.id}'] = {messages:[],scroll:0};
	    
	},
	fetchFriendMessage: (friendId) => {
	    
	    user.authFetch(chat.baseUrl + "conversationMessage", {
	            method: 'GET'
	        }, (response) => {
	            if (response.status == 400) {
	                console.log("error");
	            }
	            else if (response.status !== 200) {
	        	console.log("error");
	            }
	            else {
	            response.json().then((body) => {
	        	//
	        	if (body.hasMore){
	        	    chat.currentMessagePage++;
	        	    chat.loadPrevious = true;
	        	}
	        	
	        	body.items.forEach(message => { 
	        	let responseMessage = chat.buildMessage(responseMessage .text,responseMessage .time);
	        	chat.conversation.insertAdjacentElement('afterbegin',responseMessage );
			// chat.animateMessage(message);
	        	});
	                let list = chat.chatMessage['${friend.id}'];
	                list.messages.push(body.items);

	            });
	            }

	        });
	},
	switchToUser: (friendId) => {
	    chat.currentMessagePage = 0;
	        while (chat.conversation.lastChild) {
	            chat.conversation.lastChild.remove();
	        }
	    fetchFriendMessage(friendId);
	}
};
	
	
	

	