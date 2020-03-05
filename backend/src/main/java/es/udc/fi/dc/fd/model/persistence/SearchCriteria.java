package es.udc.fi.dc.fd.model.persistence;

import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import java.util.List;

public class SearchCriteria {

  private SexCriteriaEnum sex;

  private int minAge;

  private int maxAge;

  private List<String> city;

  private int minRate;

  public SearchCriteria() {
    super();
  }

  /**
   * Default constructor for the SearchCriteria entity.
   * @param sex The sex that will be matched
   * @param minAge The minimum age that will be matched
   * @param maxAge The maximum age that will be matched
   * @param city The list of cities that will be matched
   * @param minRate The minimum rate that will be matched
   */
  public SearchCriteria(SexCriteriaEnum sex, int minAge, int maxAge, List<String> city,
                        int minRate) {
    super();
    this.sex = sex;
    this.minAge = minAge;
    this.maxAge = maxAge;
    this.city = city;
    this.minRate = minRate;
  }

  public int getMinRate() {
    return minRate;
  }

  public void setMinRate(int minRate) {
    this.minRate = minRate;
  }

  public SexCriteriaEnum getSex() {
    return sex;
  }

  public void setSex(SexCriteriaEnum sex) {
    this.sex = sex;
  }

  public int getMinAge() {
    return minAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }

  public int getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  public List<String> getCity() {
    return city;
  }

  public void setCity(List<String> city) {
    this.city = city;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (city == null ? 0 : city.hashCode());
    result = prime * result + maxAge;
    result = prime * result + minAge;
    result = prime * result + minRate;
    result = prime * result + (sex == null ? 0 : sex.hashCode());
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
    final SearchCriteria other = (SearchCriteria) obj;
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (maxAge != other.maxAge) {
      return false;
    }
    if (minAge != other.minAge) {
      return false;
    }
    if (minRate != other.minRate) {
      return false;
    }
    if (sex != other.sex) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "SearchCriteria [sex=" + sex + ", minAge=" + minAge + ", maxAge=" + maxAge + ", city="
        + city + ", minRate=" + minRate + "]";
  }


}
