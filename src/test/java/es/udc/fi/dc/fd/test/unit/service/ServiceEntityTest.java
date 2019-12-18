package es.udc.fi.dc.fd.test.unit.service;


import org.junit.jupiter.api.Test;

import es.udc.fi.dc.fd.service.*;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;

class ServiceEntityTest {

	@Test
	void testBlock() {
		EntityTestUtils.testEntity(Block.class);
	}

}
