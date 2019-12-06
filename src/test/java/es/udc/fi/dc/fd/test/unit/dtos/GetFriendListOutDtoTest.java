package es.udc.fi.dc.fd.test.unit.dtos;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.dtos.GetFriendListOutDto;

public class GetFriendListOutDtoTest {

	@Test
	public void testHash() {
		final GetFriendListOutDto list1 = new GetFriendListOutDto();
		final GetFriendListOutDto list2 = new GetFriendListOutDto();

		assertEquals(list1.hashCode(), list2.hashCode());
	}

	@Test
	public void testEquals() {
		final GetFriendListOutDto list1 = new GetFriendListOutDto();
		final GetFriendListOutDto list2 = new GetFriendListOutDto();

		final Long id = 1L;
		list2.setId(id);
		assertFalse(list1.equals(list2));
		list1.setId(2L);
		assertFalse(list1.equals(list2));
		list1.setId(id);
		assertTrue(list1.equals(list2));

		final String sex = "Yes";
		list2.setSex(sex);
		assertFalse(list1.equals(list2));
		list1.setSex(sex + "asd");
		assertFalse(list1.equals(list2));
		list1.setSex(sex);
		assertTrue(list1.equals(list2));

		final String userName = "uaser";
		list2.setUserName(userName);
		assertFalse(list1.equals(list2));
		list1.setUserName(userName + "asda");
		assertFalse(list1.equals(list2));
		list1.setUserName(userName);

	}

}
