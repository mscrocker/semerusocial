package es.udc.fi.dc.fd.test.unit.dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import es.udc.fi.dc.fd.dtos.MessageDetailsDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class MessageDetailsDtoTest {

  @Test
  void testHashNull() {
    final MessageDetailsDto msg1 = new MessageDetailsDto();
    final MessageDetailsDto msg2 = new MessageDetailsDto();

    assertEquals(msg1.hashCode(), msg2.hashCode());
  }

  @Test
  void testEquals() {
    final MessageDetailsDto msg1 = new MessageDetailsDto();
    final MessageDetailsDto msg2 = new MessageDetailsDto();

    final LocalDateTime date = LocalDateTime.now();
    final LocalDateTime date2 = LocalDateTime.now().plusDays(2);
    msg2.setDate(date);
    assertFalse(msg1.equals(msg2));
    msg1.setDate(date2);
    assertFalse(msg1.equals(msg2));
    msg1.setDate(date);
    assertTrue(msg1.equals(msg2));

    final String messageContent = "asda";
    msg2.setMessageContent(messageContent);
    assertFalse(msg1.equals(msg2));
    msg1.setMessageContent(messageContent);
    assertTrue(msg1.equals(msg2));

    final Long owner = 1L;
    msg2.setOwner(owner);
    assertFalse(msg1.equals(msg2));
    msg1.setOwner(owner);
    assertTrue(msg1.equals(msg2));

    final Long receiver = 1L;
    msg2.setReceiver(receiver);
    assertFalse(msg1.equals(msg2));
    msg1.setReceiver(receiver);
    assertTrue(msg1.equals(msg2));
  }
}
