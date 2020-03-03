package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import org.springframework.data.domain.Slice;

public interface MessageRepositoryCustom {

  Slice<FriendChatTitle> getLatestConversations(Long userId, int page, int size);
}
