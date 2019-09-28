package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.User;

@Entity(name = "User")
@Table(name = "UserTable")
public class UserImpl implements User{

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
    
    public UserImpl() {
        super();
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
		this.id = checkNotNull(userId, "Received a null pointer as id in UserImpl");
	}

	@Override
	public void setUserName(String userName) {
		this.userName = checkNotNull(userName, "Received a null pointer as userName in UserImpl");
	}

	@Override
	public void setPassword(String password) {
		this.password = checkNotNull(password, "Received a null pointer as password in UserImpl");
	}
	
	@Override
	public void setSex(String sex) {
		this.sex = checkNotNull(sex, "Received a null pointer as sex in UserImpl");
	}

	@Override
	public void setCity(String city) {
		this.city = checkNotNull(city, "Received a null pointer as city in UserImpl");
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserImpl other = (UserImpl) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "UserImpl [id=" + id + ", userName=" + userName + ", password=" + password + ", date=" + date + ", sex="
				+ sex + ", city=" + city + "]";
	}


	public UserImpl(String userName, String password, LocalDateTime date, String sex, String city) {
		super();
		this.userName = userName;
		this.password = password;
		this.date = date;
		this.sex = sex;
		this.city = city;
	}


	
	
}
