package es.udc.fi.dc.fd.controller.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		LOGGER.info("Received a new web socket connection");

	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

		if (event == null) {
			return;
		}



		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		if (headerAccessor != null) {
			Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
			if (sessionAttributes == null) {
				return;
			}
			String username = (String) sessionAttributes.get("username");
			if (username != null) {
				LOGGER.info("User Disconnected : " + username);

				ChatMessage chatMessage = new ChatMessage();
				chatMessage.setSender(username);

				messagingTemplate.convertAndSend("/topic/public", chatMessage);
			}
		}

	}
}
