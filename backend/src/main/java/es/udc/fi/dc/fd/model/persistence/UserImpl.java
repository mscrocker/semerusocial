package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import es.udc.fi.dc.fd.model.SexCriteriaEnum;
import es.udc.fi.dc.fd.model.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "User")
@Table(name = "UserTable")
public class UserImpl implements User {

  private static final long DEFAULT_RATING_VOTES = 0;

  private static final long DEFAULT_RATING = 0;

  private static final int DEFAULT_MINRATE = 1;

  private static final boolean DEFAULT_PREMIUM = false;

  @Transient
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "userName", nullable = false, unique = true)
  private String userName;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "date")
  private LocalDateTime date;

  @Column(name = "sex")
  private String sex;

  @Column(name = "city")
  private String city;

  @Column(name = "suggestion")
  private User suggestion;

  @Enumerated(EnumType.STRING)
  @Column(name = "criteriaSex")
  private SexCriteriaEnum criteriaSex = DefaultCriteria.DEFAULT_CRITERIA_SEX;

  @Column(name = "criteriaMinAge")
  private int criteriaMinAge = DefaultCriteria.DEFAULT_CRITERIA_MIN_AGE;

  @Column(name = "criteriaMaxAge")
  private int criteriaMaxAge = DefaultCriteria.DEFAULT_CRITERIA_MAX_AGE;

  @Column(name = "description")
  private String description;

  @Column(name = "rating")
  private double rating = DEFAULT_RATING;

  @Column(name = "ratingVotes")
  private long ratingVotes = DEFAULT_RATING_VOTES;

  @Column(name = "premium")
  private boolean premium = DEFAULT_PREMIUM;

  @Column(name = "minRateCriteria")
  private int minRateCriteria = DEFAULT_MINRATE;

  public UserImpl() {
    super();
  }

  /**
   * Constructor for the user entity without rating.
   *
   * @param userName    The userName of the user
   * @param password    The password of the user
   * @param date        The date of the user
   * @param sex         The sex of the user
   * @param city        The city of the user
   * @param description The description of the user
   */
  public UserImpl(String userName, String password, LocalDateTime date, String sex, String city,
                  String description) {
    super();
    this.userName = userName;
    this.password = password;
    this.date = date;
    this.sex = sex;
    this.city = city;
    this.description = description;
  }

  /**
   * Constructor for the user entity with rating.
   *
   * @param userName    The userName of the user
   * @param password    The password of the user
   * @param date        The date of the user
   * @param sex         The sex of the user
   * @param city        The city of the user
   * @param description The description of the user
   * @param rating      The rating of the user
   * @param ratingVotes The number of votes made to the user
   */
  public UserImpl(String userName, String password, LocalDateTime date, String sex, String city,
                  String description, double rating, long ratingVotes) {
    super();
    this.userName = userName;
    this.password = password;
    this.date = date;
    this.sex = sex;
    this.city = city;
    this.description = description;
    this.rating = rating;
    this.ratingVotes = ratingVotes;
  }

  /**
   * Constructor for the user entity with only the date, sex, city and description.
   *
   * @param date        The date of the user
   * @param sex         The sex of the user
   * @param city        The city of the user
   * @param description The description of the user
   */
  public UserImpl(LocalDateTime date, String sex, String city, String description) {
    super();
    this.date = date;
    this.sex = sex;
    this.city = city;
    this.description = description;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getSex() {
    return sex;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setId(Long userId) {
    this.id = checkNotNull(userId,
        "Received a null pointer as id in UserImpl");
  }

  @Override
  public void setUserName(String userName) {
    this.userName = checkNotNull(userName,
        "Received a null pointer as userName in UserImpl");
  }

  @Override
  public void setPassword(String password) {
    this.password = checkNotNull(password,
        "Received a null pointer as password in UserImpl");
  }

  @Override
  public void setSex(String sex) {
    this.sex = checkNotNull(sex,
        "Received a null pointer as sex in UserImpl");
  }

  @Override
  public void setCity(String city) {
    this.city = checkNotNull(city,
        "Received a null pointer as city in UserImpl");
  }

  @Override
  public LocalDateTime getDate() {
    return date;
  }

  @Override
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  @Override
  public User getSuggestion() {
    return suggestion;
  }

  @Override
  public void setSuggestion(User suggestion) {
    this.suggestion = suggestion;
  }

  @Override
  public SexCriteriaEnum getCriteriaSex() {
    return criteriaSex;
  }

  @Override
  public void setCriteriaSex(SexCriteriaEnum criteriaSex) {
    this.criteriaSex = criteriaSex;
  }

  @Override
  public int getCriteriaMinAge() {
    return criteriaMinAge;
  }

  @Override
  public void setCriteriaMinAge(int criteriaMinAge) {
    this.criteriaMinAge = criteriaMinAge;
  }

  @Override
  public int getCriteriaMaxAge() {
    return criteriaMaxAge;
  }

  @Override
  public void setCriteriaMaxAge(int criteriaMaxAge) {
    this.criteriaMaxAge = criteriaMaxAge;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public double getRating() {
    return rating;
  }

  @Override
  public void setRating(double rating) {
    this.rating = rating;
  }

  @Override
  public int getMinRateCriteria() {
    return minRateCriteria;
  }

  @Override
  public void setMinRateCriteria(int minRateCriteria) {
    this.minRateCriteria = minRateCriteria;
  }

  @Override
  public long getRatingVotes() {
    return ratingVotes;
  }

  @Override
  public void setRatingVotes(long ratingVotes) {
    this.ratingVotes = ratingVotes;
  }

  @Override
  public boolean isPremium() {
    return premium;
  }

  @Override
  public void setPremium(boolean premium) {
    this.premium = premium;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (city == null ? 0 : city.hashCode());
    result = prime * result + criteriaMaxAge;
    result = prime * result + criteriaMinAge;
    result = prime * result + (criteriaSex == null ? 0 : criteriaSex.hashCode());
    result = prime * result + (date == null ? 0 : date.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (id == null ? 0 : id.hashCode());
    result = prime * result + minRateCriteria;
    result = prime * result + (password == null ? 0 : password.hashCode());
    result = prime * result + (premium ? 1231 : 1237);
    long temp;
    temp = Double.doubleToLongBits(rating);
    result = prime * result + (int) (temp ^ temp >>> 32);
    result = prime * result + (int) (ratingVotes ^ ratingVotes >>> 32);
    result = prime * result + (sex == null ? 0 : sex.hashCode());
    result = prime * result + (suggestion == null ? 0 : suggestion.hashCode());
    result = prime * result + (userName == null ? 0 : userName.hashCode());
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
    final UserImpl other = (UserImpl) obj;
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (criteriaMaxAge != other.criteriaMaxAge) {
      return false;
    }
    if (criteriaMinAge != other.criteriaMinAge) {
      return false;
    }
    if (criteriaSex != other.criteriaSex) {
      return false;
    }
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (minRateCriteria != other.minRateCriteria) {
      return false;
    }
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (premium != other.premium) {
      return false;
    }
    if (Double.doubleToLongBits(rating) != Double.doubleToLongBits(other.rating)) {
      return false;
    }
    if (ratingVotes != other.ratingVotes) {
      return false;
    }
    if (sex == null) {
      if (other.sex != null) {
        return false;
      }
    } else if (!sex.equals(other.sex)) {
      return false;
    }
    if (suggestion == null) {
      if (other.suggestion != null) {
        return false;
      }
    } else if (!suggestion.equals(other.suggestion)) {
      return false;
    }
    if (userName == null) {
      if (other.userName != null) {
        return false;
      }
    } else if (!userName.equals(other.userName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "UserImpl [id=" + id + ", userName=" + userName + ", password=" + password + ", date="
        + date + ", sex=" + sex + ", city=" + city + ", suggestion=" + suggestion
        + ", criteriaSex=" + criteriaSex + ", criteriaMinAge=" + criteriaMinAge
        + ", criteriaMaxAge=" + criteriaMaxAge + ", description=" + description + ", rating="
        + rating + ", ratingVotes=" + ratingVotes + ", premium=" + premium + ", minRateCriteria="
        + minRateCriteria + "]";
  }

}
