package es.udc.fi.dc.fd.test.unit.model.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.FriendListOut;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

public class FriendListOutTest {

	@Test
	public void testEntity() {
		EntityTestUtils.testEntity(FriendListOut.class);
	}

	@Test
	public void testHash() {
		final FriendListOut list1 = new FriendListOut();
		final FriendListOut list2 = new FriendListOut();
		assertEquals(list1.hashCode(), list2.hashCode());
	}

	@Test
	public void testConstructor() {
		final UserImpl user = new UserImpl();
		final int myRating = 12;
		final FriendListOut list = new FriendListOut(user, myRating);

		assertEquals(user, list.getUser());
		assertEquals(myRating, list.getMyRating());
	}

	@Test
	public void testEquals() {
		final FriendListOut list1 = new FriendListOut();
		final FriendListOut list2 = new FriendListOut();

		final UserImpl user = new UserImpl();
		final UserImpl user2 = new UserImpl();
		user2.setUserName("namw");
		list2.setUser(user);
		assertFalse(list1.equals(list2));
		list1.setUser(user2);
		assertFalse(list1.equals(list2));
		list1.setUser(user);
		assertTrue(list1.equals(list2));
	}
}
