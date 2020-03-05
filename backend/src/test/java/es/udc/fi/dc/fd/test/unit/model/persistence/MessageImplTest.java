package es.udc.fi.dc.fd.test.unit.model.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class MessageImplTest {

  @Test
  public void constructorTest() {
    final long messageId = 1L;
    final UserImpl user1 = new UserImpl();
    final UserImpl user2 = new UserImpl();
    final UserImpl transmitter = user1;
    final String content = "Message";
    final LocalDateTime date = LocalDateTime.now();
    final MessageImpl message = new MessageImpl(messageId, user1, user2, transmitter, content, date);

    assertEquals(message.getMessageId(), messageId);
    assertEquals(message.getUser1(), user1);
    assertEquals(message.getUser2(), user2);
    assertEquals(message.getTransmitter(), transmitter);
  }

  @Test
  public void equalsTest() {
    final UserImpl user = new UserImpl();

    final MessageImpl message = new MessageImpl();
    final MessageImpl message2 = new MessageImpl();
    final MessageImpl messageDate1 = new MessageImpl();
    messageDate1.setDate(LocalDateTime.now());
    final MessageImpl messageDate2 = new MessageImpl();
    messageDate2.setDate(LocalDateTime.now().minusDays(3));
    final MessageImpl messageContent = new MessageImpl();
    messageContent.setMessageContent("mssg");
    final MessageImpl messageId = new MessageImpl();
    messageId.setMessageId(1L);
    final MessageImpl messageTransmitter = new MessageImpl();
    messageTransmitter.setTransmitter(user);
    final MessageImpl messageUser1 = new MessageImpl();
    messageUser1.setUser1(user);
    final MessageImpl messageUser2 = new MessageImpl();
    messageUser2.setUser2(user);

    assertTrue(message.equals(message2));
    assertFalse(message.equals(messageDate1));
    assertFalse(messageDate1.equals(messageDate2));
    assertFalse(message.equals(messageContent));
    assertFalse(message.equals(messageId));
    assertFalse(message.equals(messageTransmitter));
    assertFalse(message.equals(messageUser1));
    assertFalse(message.equals(messageUser2));
  }

  @Test
  public void hashTest() {
    final MessageImpl message = new MessageImpl();
    final MessageImpl message2 = new MessageImpl();
    assertEquals(message.hashCode(), message2.hashCode());
  }
}
