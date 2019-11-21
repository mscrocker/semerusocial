package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public class MessageConversor {

	private MessageConversor() {

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
