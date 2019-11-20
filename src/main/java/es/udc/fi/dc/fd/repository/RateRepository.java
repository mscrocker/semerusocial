package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.RateId;
import es.udc.fi.dc.fd.model.persistence.RateImpl;

public interface RateRepository extends JpaRepository<RateImpl, RateId> {

	@Query("SELECT r FROM Rate r WHERE r.rateId.subject = ?1 OR r.rateId.object = ?2")
	Slice<RateImpl> findRate(Long subjectId, Long objectId);

	@Query("SELECT r FROM Rate r WHERE r.rateId.object = ?1")
	List<RateImpl> findRatesMadeToUserId(Long objectId);
}
