package es.udc.fi.dc.fd.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;


public interface ImageRepository extends JpaRepository<ImageImpl, Long> {

	Slice<ImageImpl> findByUserUserIdOrderByImageIdDesc(Long UserId, Pageable pageable);
}
