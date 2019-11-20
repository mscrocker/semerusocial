let chat = {
    usernamePage: null,
    chatPage: null,
    usernameForm: null,
    messageForm: null,
    messageInput: null,
    messageArea: null,
    connectingElement: null,
    stompClient: null,
    username: null,
    baseURL: null,
    init: (baseURL) => {
        chat.usernamePage = document.querySelector('#username-page');
        chat.chatPage = document.querySelector('#chat-page');
        chat.usernameForm = document.querySelector('#usernameForm');
        chat.messageForm = document.querySelector('#messageForm');
        chat.messageInput = document.querySelector('#message');
        chat.messageArea = document.querySelector('#messageArea');
        chat.connectingElement = document.querySelector('.connecting');
        chat.baseURL = baseURL;
        var colors = [
            '#2196F3', '#32c787', '#00BCD4', '#ff5652',
            '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
        ];
        chat.messageForm.addEventListener('submit', chat.sendMessage, true);
        chat.connect();
    },

    connect: () => {
        chat.usernamePage.classList.add('hidden');
        chat.chatPage.classList.remove('hidden');

        var socket = new SockJS(chat.baseURL + '/ws');
        chat.stompClient = Stomp.over(socket);

        chat.stompClient.connect({
            'X-Authorization': localStorage.user_jwt
        }, chat.onConnected, chat.onError);
    },



    onConnected: () => {
        // / / Subscribe to the Public Topic
        chat.stompClient.subscribe("/user/queue/reply", chat.onMessageReceived, {
            'X-Authorization': localStorage.user_jwt
        });
        chat.connectingElement.classList.add('hidden');
    },


    onError: (error) => {
        chat.connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        chat.connectingElement.style.color = 'red';
    },


    sendMessage: (event) => {
        var messageContent = chat.messageInput.value.trim();
        if (messageContent && chat.stompClient) {
            var chatMessage = {
                sender: chat.username,
                content: chat.messageInput.value,
                receiver: window.location.pathname.split("/").pop(),
                receiverId: window.location.pathname.split("/").pop(),
                type: 'CHAT'
            };
            chat.stompClient.send('/app/chat.sendMessage', {
                'X-Authorization': localStorage.user_jwt
            }, JSON.stringify(chatMessage));
            chat.messageInput.value = '';
        }
        event.preventDefault();
    },


    onMessageReceived: (payload) => {
        var message = JSON.parse(payload.body);

        var messageElement = document.createElement('li');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' left!';
        } else {
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.sender);
            avatarElement.appendChild(avatarText);
       // avatarElement.style['background-color'] =
	// chat.getAvatarColor(message.sender);

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        chat.messageArea.appendChild(messageElement);
        chat.messageArea.scrollTop = chat.messageArea.scrollHeight;
    },


    getAvatarColor: (messageSender) => {
        var hash = 0;
        for (var i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }
        var index = Math.abs(hash % colors.length);
        return colors[index];
    },

};