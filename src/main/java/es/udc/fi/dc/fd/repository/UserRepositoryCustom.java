package es.udc.fi.dc.fd.repository;

import java.util.Optional;

import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepositoryCustom {

	Optional<UserImpl> findByCriteria(SearchCriteria criteria, Long userId);

	int findByCriteriaMaxResults(SearchCriteria criteria, Long userId);
}
