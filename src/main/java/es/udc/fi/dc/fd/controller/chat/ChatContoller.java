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
import es.udc.fi.dc.fd.controller.exception.RequestParamException;
import es.udc.fi.dc.fd.jwt.JwtInfo;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.BlockFriendList;
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
		try {
			BlockFriendList<UserImpl> friendList = friendService.getFriendList(chatMessage.getReceiverId(), 0,
					Integer.MAX_VALUE);
			long count = friendList.getFriends().stream().map(x -> x.getUserName())
					.filter(x -> x.equals(user.getName())).count();
			if (count == 1 || ((JwtInfo) user).getUserId().equals(chatMessage.getReceiverId())) {
				UserImpl whomToSend = userService.loginFromUserId(chatMessage.getReceiverId());
				// TODO store message on db
				messagingTemplate.convertAndSendToUser(whomToSend.getUserName(), "/queue/reply", chatMessage);
			}

		} catch (InstanceNotFoundException e) {

		} catch (RequestParamException e) {
			e.printStackTrace();
		}
		logger.info("Illegal access to chat");
		System.out.println("other logger");
		return;
	}
}