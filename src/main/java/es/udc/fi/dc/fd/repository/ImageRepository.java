package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public interface ImageRepository extends JpaRepository<ImageImpl, Long> {
	
}
