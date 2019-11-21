package es.udc.fi.dc.fd.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.FriendChatTitle;
import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public interface MessageRepository extends JpaRepository<MessageImpl, Long>, MessageRepositoryCustom {

	
	
	
}
