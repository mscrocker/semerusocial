package es.udc.fi.dc.fd.dtos;

import java.util.stream.Collectors;

import es.udc.fi.dc.fd.controller.chat.MessageType;
import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import es.udc.fi.dc.fd.service.Block;

public class MessageConversor {
	public final static FriendHeaderDto toFriendHeaderDto(FriendChatTitle title) {
		return (FriendHeaderDto) title;
	}

	public final static BlockDto<FriendHeaderDto> toFriendHeadersDto(Block<FriendChatTitle> messages) {
		return new BlockDto<>(
				messages.getElements().stream().map(m -> toFriendHeaderDto(m)).collect(Collectors.toList()),
				messages.isExistMoreElements());
	}

}
