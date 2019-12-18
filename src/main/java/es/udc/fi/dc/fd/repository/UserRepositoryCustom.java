package es.udc.fi.dc.fd.repository;

import java.util.List;

import es.udc.fi.dc.fd.model.persistence.SearchCriteria;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface UserRepositoryCustom {

	List<UserImpl> findByCriteria(SearchCriteria criteria, Long userId);
}
