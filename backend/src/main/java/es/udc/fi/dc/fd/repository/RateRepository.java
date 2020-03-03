package es.udc.fi.dc.fd.repository;

import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateImpl, RateId> {

}
