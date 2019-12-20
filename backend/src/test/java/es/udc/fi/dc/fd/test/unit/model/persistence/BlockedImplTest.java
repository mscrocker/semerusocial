package es.udc.fi.dc.fd.test.unit.model.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.BlockedImpl;

public class BlockedImplTest {

	@Test
	public void constructorTest() {
		final long subject = 1L;
		final long object = 2L;
		final LocalDateTime date = LocalDateTime.now();
		final BlockedId id = new BlockedId(subject, object);
		final BlockedImpl block = new BlockedImpl(id, date);

		assertEquals(block.getBlockedId().getSubject(), subject);
		assertEquals(block.getBlockedId().getObject(), object);
		assertEquals(block.getDate(), date);
	}


	@Test
	public void equalsTest() {
		final BlockedId id = new BlockedId(1L, 2L);

		final BlockedImpl block1 = new BlockedImpl();
		final BlockedImpl block2 = new BlockedImpl();
		final BlockedImpl blockId = new BlockedImpl();
		blockId.setBlockedId(id);
		final BlockedImpl blockDate = new BlockedImpl();
		blockDate.setDate(LocalDateTime.now());

		assertTrue(block1.equals(block2));
		assertFalse(block1.equals(blockId));
		assertFalse(block1.equals(blockDate));
	}

	@Test
	public void hashTest() {

		final BlockedImpl block1 = new BlockedImpl();
		final BlockedImpl block2 = new BlockedImpl();

		assertEquals(block1.hashCode(), block2.hashCode());
	}

	@Test
	public void idHashTest() {
		final BlockedId id1 = new BlockedId();
		final BlockedId id2 = new BlockedId();
		assertEquals(id1.hashCode(), id2.hashCode());
	}

	@Test
	public void idEqualsTest() {
		final BlockedId id1 = new BlockedId();
		final BlockedId id2 = new BlockedId();
		final BlockedId idObject = new BlockedId();
		idObject.setObject(1L);
		final BlockedId idSubject = new BlockedId();
		idSubject.setSubject(1L);

		assertTrue(id1.equals(id2));
		assertFalse(id1.equals(idObject));
		assertFalse(id1.equals(idSubject));
	}
}

