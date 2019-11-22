package es.udc.fi.dc.fd.dtos;

import java.util.stream.Collectors;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.service.Block;

public class MessageConversor {

	private MessageConversor() {

	}

	public static final FriendHeaderDto toFriendHeaderDto(FriendChatTitle title) {
		return new FriendHeaderDto(
				title.getFriendId(),
				title.getFriendName(),
				title.getContent(),
				title.getSentByYou(),
				title.getDate()
				);
	}

	public static final BlockDto<FriendHeaderDto> toFriendHeadersDto(Block<FriendChatTitle> messages) {
		return new BlockDto<>(
				messages.getElements().stream().map(m -> toFriendHeaderDto(m)).collect(Collectors.toList()),
				messages.isExistMoreElements());
	}

	public static final MessageDetailsDto messageToMessageDetailsDto(MessageImpl msg) {
		Long receiver;
		if ( msg.getUser1().equals(msg.getTransmitter())) {
			receiver= msg.getUser1().getId();
		} else {
			receiver = msg.getUser2().getId();
		}
		return new MessageDetailsDto(msg.getMessageContent(), msg.getDate(),
				msg.getTransmitter().getId(), receiver);
	}

}
