package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

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
    @Column(name = "userId", nullable = false, unique = true)
    private Long userId = -1L;

    /**
     * userName of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "userName", nullable = false, unique = true)
    private String userName = "";

    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "password", nullable = false)
    private String password = "";
    
    /**
     * Constructs an example entity.
     */
    public UserImpl() {
        super();
    }
    
	@Override
	public Long getUserId() {
		return userId;
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
	public void setUserId(Long userId) {
		this.userId = checkNotNull(userId, "Received a null pointer as userId");
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
    public final boolean equals(final Object obj) {
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
        return Objects.equals(userId, other.userId);
    }
	
	@Override
    public final int hashCode() {
        return Objects.hash(userId);
    }

	@Override
	public String toString() {
		return "UserImpl [userId=" + userId + ", userName=" + userName + ", password=" + password + "]";
	}
	
	

}
