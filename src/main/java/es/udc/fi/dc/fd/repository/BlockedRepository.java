package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.BlockedImpl;

public interface BlockedRepository extends JpaRepository<BlockedImpl, BlockedId> {

}
