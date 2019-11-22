package es.udc.fi.dc.fd.test.unit.dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.dtos.BlockDto;
import es.udc.fi.dc.fd.dtos.FriendHeaderDto;
import es.udc.fi.dc.fd.dtos.MessageConversor;
import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.service.Block;

public class MessageConversorTest {

	@Test
	public void testToFriendHeadersDto() {

		final BlockDto<FriendHeaderDto> result = MessageConversor
				.toFriendHeadersDto(new Block<>(Arrays.asList(new FriendChatTitle[] {
						new FriendChatTitle(1L, "testFriend", "hello", true, LocalDateTime.now().minusMinutes(1L)),
						new FriendChatTitle(2L, "testFriend2", "goodbye", false,
								LocalDateTime.now().minusMinutes(2L)) }),
						false));

		assertFalse(result.isExistMoreElements());
		assertTrue(result.getElements().size() == 2);
		assertEquals("testFriend", result.getElements().get(0).getFriendName());
		assertEquals("testFriend2", result.getElements().get(1).getFriendName());
		assertEquals("hello", result.getElements().get(0).getContent());
		assertEquals("goodbye", result.getElements().get(1).getContent());
	}

	@Test
	void testToImageCreationDto() {
		final UserImpl user = new UserImpl();
		user.setId(1L);
		final UserImpl user2 = new UserImpl();
		user2.setId(2L);

		final LocalDateTime date = LocalDateTime.now();
		final String messageContent = "messageContent";
		final Long messageId = 1L;

		final MessageImpl msg = new MessageImpl();
		msg.setDate(date);
		msg.setMessageContent(messageContent);
		msg.setMessageId(messageId);
		msg.setTransmitter(user);
		msg.setUser1(user);
		msg.setUser2(user2);
		final MessageDetailsDto dto = MessageConversor.messageToMessageDetailsDto(msg);

		assertEquals(dto.getDate(), date);
		assertEquals(dto.getMessageContent(), messageContent);
		assertEquals(dto.getOwner(), user.getId());
		assertEquals(dto.getReceiver(), user.getId());

		msg.setTransmitter(user2);
		final MessageDetailsDto dto2 = MessageConversor.messageToMessageDetailsDto(msg);

		assertEquals(dto2.getOwner(), user2.getId());
	}

}
