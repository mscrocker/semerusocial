package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.Rate;
import es.udc.fi.dc.fd.model.persistence.RateId;

public interface RateRepository extends JpaRepository<Rate, RateId> {

}
