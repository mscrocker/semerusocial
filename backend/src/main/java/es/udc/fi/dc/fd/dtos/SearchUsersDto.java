package es.udc.fi.dc.fd.dtos;

public class SearchUsersDto {

  private String keywords;

  private SearchCriteriaDto metadata;

  /**
   * Default constructor for the SearchUsersDTO.
   * @param keywords The keywords for the search
   * @param metadata The search criteria
   */
  public SearchUsersDto(String keywords, SearchCriteriaDto metadata) {
    super();
    this.setKeywords(keywords);
    this.setMetadata(metadata);
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
    result = prime * result + (getKeywords() == null ? 0 : getKeywords().hashCode());
    result = prime * result + (getMetadata() == null ? 0 : getMetadata().hashCode());
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
    if (getKeywords() == null) {
      if (other.getKeywords() != null) {
        return false;
      }
    } else if (!getKeywords().equals(other.getKeywords())) {
      return false;
    }
    if (getMetadata() == null) {
      if (other.getMetadata() != null) {
        return false;
      }
    } else if (!getMetadata().equals(other.getMetadata())) {
      return false;
    }
    return true;
  }

}
