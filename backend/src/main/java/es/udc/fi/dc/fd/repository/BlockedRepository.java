package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.BlockedId;
import es.udc.fi.dc.fd.model.persistence.BlockedImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedRepository extends JpaRepository<BlockedImpl, BlockedId> {

}
