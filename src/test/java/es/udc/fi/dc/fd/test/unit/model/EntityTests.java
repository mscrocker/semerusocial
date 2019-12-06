package es.udc.fi.dc.fd.test.unit.model;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

public class EntityTests {
	@Test
	public void testFriendChatTitle() {
		EntityTestUtils.testEntity(FriendChatTitle.class);
	}
}
