package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.MessageImpl;

public interface MessageRepository extends JpaRepository<MessageImpl, Long> {

}
