package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.SearchCriteria;

public class SearchCriteriaConversor {

	public final static SearchCriteriaDto toSearchCriteriaDto(SearchCriteria criteria) {

		return new SearchCriteriaDto(criteria.getSex().getCode(), criteria.getMinAge(),
			criteria.getMaxAge(), criteria.getCity(), criteria.getMinRate());
	}

}
