package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaImpl;

public interface CityCriteriaRepository extends JpaRepository<CityCriteriaImpl, CityCriteriaId>{

}
