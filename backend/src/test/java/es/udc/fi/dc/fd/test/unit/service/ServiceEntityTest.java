package es.udc.fi.dc.fd.test.unit.service;


import es.udc.fi.dc.fd.service.Block;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;
import org.junit.jupiter.api.Test;

class ServiceEntityTest {

  @Test
  void testBlock() {
    EntityTestUtils.testEntity(Block.class);
  }

}
