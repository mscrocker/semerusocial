package es.udc.fi.dc.fd.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public interface MessageRepositoryCustom {
	
	Slice<FriendChatTitle> getLatestConversations(Long userId, int page, int size);
}
