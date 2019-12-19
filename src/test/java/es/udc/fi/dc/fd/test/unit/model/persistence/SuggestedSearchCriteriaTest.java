package es.udc.fi.dc.fd.test.unit.model.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.SuggestedSearchCriteria;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

public class SuggestedSearchCriteriaTest {
	@Test
	public void testEntity() {
		EntityTestUtils.testEntity(SuggestedSearchCriteria.class);
	}

	@Test
	public void testHash() {
		final SuggestedSearchCriteria list1 = new SuggestedSearchCriteria();
		final SuggestedSearchCriteria list2 = new SuggestedSearchCriteria();
		assertEquals(list1.hashCode(), list2.hashCode());
	}

	@Test
	public void testConstructor() {
		final SuggestedSearchCriteria suggestedCriteria = new SuggestedSearchCriteria(-1, 1, 1, 1);

		assertEquals(-1, suggestedCriteria.getNewMinAge());
		assertEquals(1, suggestedCriteria.getNewMaxAge());
		assertEquals(1, suggestedCriteria.getNewMinRate());
		assertEquals(1, suggestedCriteria.getUsersYouWouldFind());
	}

	@Test
	public void testEquals() {

		SuggestedSearchCriteria suggestedCriteria1 = new SuggestedSearchCriteria();
		SuggestedSearchCriteria suggestedCriteria2 = new SuggestedSearchCriteria();

		suggestedCriteria1 = new SuggestedSearchCriteria(1, 1, 1, 1);
		suggestedCriteria2 = new SuggestedSearchCriteria(0, 1, 1, 1);
		assertFalse(suggestedCriteria1.equals(suggestedCriteria2));

		suggestedCriteria1 = new SuggestedSearchCriteria(1, 1, 1, 1);
		suggestedCriteria2 = new SuggestedSearchCriteria(1, 0, 1, 1);
		assertFalse(suggestedCriteria1.equals(suggestedCriteria2));

		suggestedCriteria1 = new SuggestedSearchCriteria(1, 1, 1, 1);
		suggestedCriteria2 = new SuggestedSearchCriteria(1, 1, 0, 1);
		assertFalse(suggestedCriteria1.equals(suggestedCriteria2));

		suggestedCriteria1 = new SuggestedSearchCriteria(1, 1, 1, 1);
		suggestedCriteria2 = new SuggestedSearchCriteria(1, 1, 1, 0);
		assertFalse(suggestedCriteria1.equals(suggestedCriteria2));

		suggestedCriteria2.setUsersYouWouldFind(1);
		assertTrue(suggestedCriteria1.equals(suggestedCriteria2));
	}
}

