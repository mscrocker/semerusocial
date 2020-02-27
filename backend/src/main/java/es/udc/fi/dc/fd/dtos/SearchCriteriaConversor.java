package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.SearchCriteria;

public final class SearchCriteriaConversor {

	public static final SearchCriteriaDto toSearchCriteriaDto(SearchCriteria criteria) {

		return new SearchCriteriaDto(criteria.getSex().getCode(), criteria.getMinAge(),
			criteria.getMaxAge(), criteria.getCity(), criteria.getMinRate());
	}

	private SearchCriteriaConversor() {

	}

}
