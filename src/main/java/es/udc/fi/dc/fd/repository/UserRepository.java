package es.udc.fi.dc.fd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepository extends JpaRepository<UserImpl, Long> {
	
	boolean existsByUserName(String userName);
	
	Optional<UserImpl> findByUserName(String userName);
	
	Optional<UserImpl> findById (long userId);
}
