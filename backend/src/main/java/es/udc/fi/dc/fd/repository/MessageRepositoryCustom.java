package es.udc.fi.dc.fd.repository;

import org.springframework.data.domain.Slice;

import es.udc.fi.dc.fd.model.FriendChatTitle;

public interface MessageRepositoryCustom {

	Slice<FriendChatTitle> getLatestConversations(Long userId, int page, int size);
}
