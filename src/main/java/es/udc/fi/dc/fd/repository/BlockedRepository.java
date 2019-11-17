package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.Blocked;
import es.udc.fi.dc.fd.model.persistence.BlockedId;

public interface BlockedRepository extends JpaRepository<Blocked, BlockedId> {

}
