package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.MessageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<MessageImpl, Long>,
    MessageRepositoryCustom {

  // userId<userId2
  @Query(value = "SELECT m FROM Message m WHERE m.user1.id = ?1 AND m.user2.id = ?2 ORDER BY "
      + "m.date desc ")
  Slice<MessageImpl> findMessagesByUsersId(Long userId, Long userId2, Pageable pageable);

}
