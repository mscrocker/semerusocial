package es.udc.fi.dc.fd.test.unit.model.persistence;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.BlockedImpl;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaImpl;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.MatchId;
import es.udc.fi.dc.fd.model.persistence.MatchImpl;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;
import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import es.udc.fi.dc.fd.model.persistence.RequestId;
import es.udc.fi.dc.fd.model.persistence.RequestImpl;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

import java.util.Arrays;

class EntityImplTest {

	@Test
	void testUserImpl() {
		EntityTestUtils.testEntity(UserImpl.class);
	}

	@Test
	void testSearchCriteria() {
		EntityTestUtils.testEntity(SearchCriteria.class);
	}

	@Test
	void testRequestImpl() {
		EntityTestUtils.testEntity(RequestImpl.class);
	}

	@Test
	void testRequestId() {
		EntityTestUtils.testEntity(RequestId.class);
	}

	@Test
	void testRejectedImpl() {
		EntityTestUtils.testEntity(RejectedImpl.class);
	}

	@Test
	void testRejectedId() {
		EntityTestUtils.testEntity(RejectedId.class);
	}

	@Test
	void testMatchImpl() {
		EntityTestUtils.testEntity(MatchImpl.class);
	}

	@Test
	void testMatchId() {
		EntityTestUtils.testEntity(MatchId.class);
	}

	@Test
	void testImageImpl() {
		EntityTestUtils.testEntity(ImageImpl.class,
			Arrays.asList(new String[]{"data"}),
			Arrays.asList(new String[]{"getData"}),
			Arrays.asList(new String[]{"setData"}),
			Arrays.asList(new String[]{"data"}));
	}

	@Test
	void testCityCriteriaImpl() {
		EntityTestUtils.testEntity(CityCriteriaImpl.class);
	}

	@Test
	void testCityCriteriaId() {
		EntityTestUtils.testEntity(CityCriteriaId.class);
	}

	@Test
	void testRateImpl() {
		EntityTestUtils.testEntity(RateImpl.class);
	}

	@Test
	void testRateId() {
		EntityTestUtils.testEntity(RateId.class);
	}

	@Test
	void testMessageImpl() {
		EntityTestUtils.testEntity(MessageImpl.class);
	}

	@Test
	void testBlockedImpl() {
		EntityTestUtils.testEntity(BlockedImpl.class);
	}

	@Test
	void testBlockedId() {
		EntityTestUtils.testEntity(BlockedId.class);
	}

}
