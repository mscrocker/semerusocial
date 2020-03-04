package es.udc.fi.dc.fd.test.unit.model;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.test.utils.EntityTestUtils;
import org.junit.jupiter.api.Test;

public class EntityTests {
  @Test
  public void testFriendChatTitle() {
    EntityTestUtils.testEntity(FriendChatTitle.class);
  }
}
