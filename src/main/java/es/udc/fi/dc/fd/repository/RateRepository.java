package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;

public interface RateRepository extends JpaRepository<RateImpl, RateId> {

}
