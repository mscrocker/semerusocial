package es.udc.fi.dc.fd.repository;

import java.util.Optional;

import es.udc.fi.dc.fd.dtos.SearchCriteriaDto;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepositoryCustom {

	Optional<UserImpl> findByCriteria(SearchCriteriaDto criteria, Long userId);
}
