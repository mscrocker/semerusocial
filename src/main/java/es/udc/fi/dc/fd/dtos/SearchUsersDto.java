package es.udc.fi.dc.fd.dtos;

public class SearchUsersDto {

	public String keywords;

	public SearchCriteriaDto metadata;

	public SearchUsersDto(String keywords, SearchCriteriaDto metadata) {
		super();
		this.keywords = keywords;
		this.metadata = metadata;
	}

	public SearchUsersDto() {
		super();
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public SearchCriteriaDto getMetadata() {
		return metadata;
	}

	public void setMetadata(SearchCriteriaDto metadata) {
		this.metadata = metadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (keywords == null ? 0 : keywords.hashCode());
		result = prime * result + (metadata == null ? 0 : metadata.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SearchUsersDto other = (SearchUsersDto) obj;
		if (keywords == null) {
			if (other.keywords != null) {
				return false;
			}
		} else if (!keywords.equals(other.keywords)) {
			return false;
		}
		if (metadata == null) {
			if (other.metadata != null) {
				return false;
			}
		} else if (!metadata.equals(other.metadata)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SearchUsersDto [keywords=" + keywords + ", metadata=" + metadata + "]";
	}

}
