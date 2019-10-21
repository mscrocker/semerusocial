package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.CityCriteriaId;
import es.udc.fi.dc.fd.model.persistence.CityCriteriaImpl;

public interface CityCriteriaRepository extends JpaRepository<CityCriteriaImpl, CityCriteriaId>{

	@Query("SELECT c.cityCriteriaId.city FROM Cities c WHERE c.cityCriteriaId.userId = ?1")
	List<String> findCitiesByUserId(Long userId);

	//Devuelve todos los pares de id de un usuario
	@Query("SELECT c.cityCriteriaId FROM Cities c WHERE c.cityCriteriaId.userId = ?1")
	List<CityCriteriaImpl> findCitiesIdByUserId(Long userId);
}
