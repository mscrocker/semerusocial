package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepository extends JpaRepository<UserImpl, Integer> {
	
}
