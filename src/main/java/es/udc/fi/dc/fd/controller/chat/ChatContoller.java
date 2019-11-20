package es.udc.fi.dc.fd.controller.chat;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.NotYourFriendException;
import es.udc.fi.dc.fd.controller.exception.ValidationException;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.FriendService;
import es.udc.fi.dc.fd.service.UserService;

@Controller
public class ChatContoller {
	private static final Logger logger = LoggerFactory.getLogger(ChatContoller.class);

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private FriendService friendService;

	@Autowired
	private UserService userService;

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage, Principal user) {
		// TODO While we dont have the areWeFriends Method, a little hack
		JwtInfo ownerUser = ((JwtInfo) user);

		try {
			UserImpl receiver = userService.loginFromUserId(chatMessage.getReceiverId());
			friendService.sendMessage(ownerUser.getUserId(), chatMessage.getReceiverId(), chatMessage.getContent());
			messagingTemplate.convertAndSendToUser(receiver.getUserName(), "/queue/reply", chatMessage);

		} catch (InstanceNotFoundException | NotYourFriendException | ValidationException e) {
			logger.info("Illegal access to chat");
			System.out.println("other logger");
			ChatMessage chatMessage2 = new ChatMessage();
			chatMessage2.setType(MessageType.ERROR);
			chatMessage2.setContent("Tried to send a message to an invalid person");
			messagingTemplate.convertAndSendToUser(ownerUser.getName(), "/queue/reply", chatMessage2);
			return;
		}
	}
}