package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.service.Block;
import java.util.stream.Collectors;

public final class MessageConversor {

  private MessageConversor() {

  }

  /**
   * Transforms a friend chat title entity to a friend header DTO.
   * @param title The friend chat title entity
   * @return The friend header DTO
   */
  public static final FriendHeaderDto toFriendHeaderDto(FriendChatTitle title) {
    return new FriendHeaderDto(title.getFriendId(), title.getFriendName(), title.getContent(),
        title.getSentByYou(),
        title.getDate());
  }

  /**
   * Transforms a block with friend chat title entities to a block dto with friend header DTOs.
   * @param messages The block with the friend chat entities
   * @return The block dto with friend header DTOs
   */
  public static final BlockDto<FriendHeaderDto> toFriendHeadersDto(
      Block<FriendChatTitle> messages) {
    return new BlockDto<>(
        messages.getElements().stream().map(m -> toFriendHeaderDto(m)).collect(Collectors.toList()),
        messages.isExistMoreElements());
  }

  /**
   * Transform a message entity to a message dto.
   * @param msg The message entity
   * @return The message DTO
   */
  public static final MessageDetailsDto messageToMessageDetailsDto(MessageImpl msg) {
    Long receiver;
    if (msg.getUser1().equals(msg.getTransmitter())) {
      receiver = msg.getUser2().getId();
    } else {
      receiver = msg.getUser1().getId();
    }
    return new MessageDetailsDto(msg.getMessageContent(), msg.getDate(),
        msg.getTransmitter().getId(), receiver);
  }

}
