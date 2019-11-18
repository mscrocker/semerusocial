package es.udc.fi.dc.fd.test.unit.model.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;

public class RateImplTest {

	@Test
	public void constructorTest() {
		final long subject = 1L;
		final long object = 2L;
		final int points = 3;

		final RateId id = new RateId(subject, object);
		final RateImpl rate = new RateImpl(id, points);

		assertEquals(subject, rate.getRateId().getSubject());
		assertEquals(object, rate.getRateId().getObject());
		assertEquals(points, rate.getPoints());
	}

	@Test
	public void equalsTest() {
		final int points = 3;
		final long subject = 1L;
		final long object = 2L;
		final RateId id = new RateId(subject, object);

		final RateImpl rate1 = new RateImpl(null, points);
		final RateImpl rate2 = new RateImpl(id, points);
		final RateImpl rate3 = new RateImpl(null, points);

		assertFalse(rate1.equals(rate2));
		assertTrue(rate1.equals(rate3));
	}

	@Test
	public void hashTest() {
		final int points = 3;
		final RateImpl rate1 = new RateImpl(null, points);
		final RateImpl rate2 = new RateImpl(null, points);

		assertEquals(rate1.hashCode(), rate2.hashCode());
	}

	@Test
	public void idHashTest() {
		final RateId id1 = new RateId();
		final RateId id2 = new RateId();
		assertEquals(id1.hashCode(), id2.hashCode());
	}

	@Test
	public void idEqualsTest() {
		final RateId id1 = new RateId();
		final RateId id2 = new RateId();
		final RateId idObject = new RateId();
		idObject.setObject(1L);
		final RateId idSubject = new RateId();
		idSubject.setSubject(1L);

		assertTrue(id1.equals(id2));
		assertFalse(id1.equals(idObject));
		assertFalse(id1.equals(idSubject));
	}
}
