package es.udc.fi.dc.fd.test.unit.model.persistence;

import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.model.persistence.*;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

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
		EntityTestUtils.testEntity(ImageImpl.class);
	}
	
	@Test
	void testCityCriteriaImpl() {
		EntityTestUtils.testEntity(CityCriteriaImpl.class);
	}
	
	@Test
	void testCityCriteriaId() {
		EntityTestUtils.testEntity(CityCriteriaId.class);
	}
	

}
