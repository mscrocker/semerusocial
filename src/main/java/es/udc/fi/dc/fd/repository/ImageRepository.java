package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;


public interface ImageRepository extends PagingAndSortingRepository<ImageImpl, Long> {

	Slice<ImageImpl> findByUserId(Long userId, Pageable pageable);
	
	List<ImageImpl> findByUserId(Long userId);
	
}
