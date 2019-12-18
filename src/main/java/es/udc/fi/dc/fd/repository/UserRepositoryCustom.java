package es.udc.fi.dc.fd.repository;

import java.util.Optional;

import org.springframework.data.domain.Slice;

import es.udc.fi.dc.fd.dtos.SearchUsersDto;
import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepositoryCustom {

	Optional<UserImpl> findByCriteria(SearchCriteria criteria, Long userId);

	Slice<UserImpl> searchUsersByMetadataAndKeywords(SearchUsersDto params, int page, int size);

}
