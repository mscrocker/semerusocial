package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
