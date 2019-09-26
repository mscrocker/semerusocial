package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

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

	/**
     * Serialization ID.
     */
    @Transient
	private static final long serialVersionUID = 1L;

    /**
     * Entity's userId.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * userName of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "password", nullable = false)
    private String password;
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "age")
    private int age;
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "sex")
    private String sex;
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "city")
    private String city;
    
    /**
     * Constructs an example entity.
     */
    public UserImpl() {
        super();
    }
    /*
    public UserImpl(String userName, String password) {
        super();
        setUserName(userName);
        setPassword(password);
    }
    */
    public UserImpl(String userName, String password, int age, String sex, String city) {
        super();
        setUserName(userName);
        setPassword(password);
        setAge(age);
        setSex(sex);
        setCity(city);
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
	public int getAge() {
		return age;
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
		this.id = checkNotNull(userId, "Received a null pointer as userId");
	}

	@Override
	public void setUserName(String userName) {
		this.userName = checkNotNull(userName, "Received a null pointer as userName");
		
	}

	@Override
	public void setPassword(String password) {
		this.password = checkNotNull(password, "Received a null pointer as password");
		
	}
	
	@Override
	public void setAge(int age) {
		this.age = checkNotNull(age, "Received a null pointer as age");
	}

	@Override
	public void setSex(String sex) {
		this.sex = checkNotNull(sex, "Received a null pointer as sex");
	}

	@Override
	public void setCity(String city) {
		this.city = checkNotNull(city, "Received a null pointer as city");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
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
		if (age != other.age)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
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
		return "UserImpl [id=" + id + ", userName=" + userName + ", password=" + password + ", age=" + age + ", sex="
				+ sex + ", city=" + city + "]";
	}
	
	

}
