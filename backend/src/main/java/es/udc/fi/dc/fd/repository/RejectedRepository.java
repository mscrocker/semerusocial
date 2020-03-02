package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.RejectedId;
import es.udc.fi.dc.fd.model.persistence.RejectedImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RejectedRepository extends JpaRepository<RejectedImpl, RejectedId> {

}
