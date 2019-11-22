package es.udc.fi.dc.fd.test.unit.controller.chat;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.controller.chat.ChatMessage;
import es.udc.fi.dc.fd.controller.chat.MessageType;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

public class ChatMessageTest {

	@Test
	void testChatMessage() {
		EntityTestUtils.testEntity(ChatMessage.class);
	}

	@Test
	void testMessageType() {
		final ChatMessage msg1 = new ChatMessage();
		msg1.setType(MessageType.CHAT);
		assertEquals(MessageType.CHAT, msg1.getType());
		msg1.setType(MessageType.ERROR);
		assertEquals(MessageType.ERROR, msg1.getType());
		msg1.setType(MessageType.JOIN);
		assertEquals(MessageType.JOIN, msg1.getType());
		msg1.setType(MessageType.LEAVE);
		assertEquals(MessageType.LEAVE, msg1.getType());

	}
}
