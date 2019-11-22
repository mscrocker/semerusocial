package es.udc.fi.dc.fd.test.unit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.FriendChatTitle;

public class TestFriendChatTitle {

	@Test
	public void testHash() {
		final FriendChatTitle title1 = new FriendChatTitle();
		final FriendChatTitle title2 = new FriendChatTitle();

		assertEquals(title1.hashCode(), title2.hashCode());
	}

	@Test
	public void testEquals() {
		final FriendChatTitle title1 = new FriendChatTitle();
		final FriendChatTitle title2 = new FriendChatTitle();

		final String content = "asads";
		title2.setContent(content);
		assertFalse(title1.equals(title2));
		title1.setContent(content + "adsa");
		assertFalse(title1.equals(title2));
		title1.setContent(content);
		assertTrue(title1.equals(title2));

		final LocalDateTime date = LocalDateTime.now();
		title2.setDate(date);
		assertFalse(title1.equals(title2));
		title1.setDate(date.plusHours(231));
		assertFalse(title1.equals(title2));
		title1.setDate(date);
		assertTrue(title1.equals(title2));

		final Long id = 1L;
		title2.setFriendId(id);
		assertFalse(title1.equals(title2));
		title1.setFriendId(id + 1L);
		assertFalse(title1.equals(title2));
		title1.setFriendId(id);
		assertTrue(title1.equals(title2));

		final String friendName = "name";
		title2.setFriendName(friendName);
		assertFalse(title1.equals(title2));
		title1.setFriendName(friendName + "AA");
		assertFalse(title1.equals(title2));
		title1.setFriendName(friendName);
		assertTrue(title1.equals(title2));

		final boolean sentByYou = true;
		title2.setSentByYou(sentByYou);
		assertFalse(title1.equals(title2));
		title1.setSentByYou(!sentByYou);
		assertFalse(title1.equals(title2));
		title1.setSentByYou(sentByYou);
		assertTrue(title1.equals(title2));

	}
}
