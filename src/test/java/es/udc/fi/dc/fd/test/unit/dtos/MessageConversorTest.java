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
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.service.Block;

public class MessageConversorTest {

	@Test
	public void testToFriendHeadersDto() {
		
		BlockDto<FriendHeaderDto> result = MessageConversor.toFriendHeadersDto(new Block<>(Arrays.asList(new FriendChatTitle[] {
			new FriendChatTitle(1L, "testFriend", "hello", true, LocalDateTime.now().minusMinutes(1L)),
			new FriendChatTitle(2L, "testFriend2", "goodbye", false, LocalDateTime.now().minusMinutes(2L))
		}), false));
		
		assertFalse(result.isExistMoreElements());
		assertTrue(result.getElements().size() == 2);
		assertEquals("testFriend", result.getElements().get(0).getFriendName());
		assertEquals("testFriend2", result.getElements().get(1).getFriendName());
		assertEquals("hello", result.getElements().get(0).getContent());
		assertEquals("goodbye", result.getElements().get(1).getContent());
	}
}
