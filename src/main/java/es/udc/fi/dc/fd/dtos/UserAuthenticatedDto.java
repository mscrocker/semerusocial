package es.udc.fi.dc.fd.dtos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public final class UserAuthenticatedDto implements Serializable {

    private static final long serialVersionUID = 1328776989450853492L;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String jwt;

    public UserAuthenticatedDto() {
        super();
    }
    
    public UserAuthenticatedDto(String userName, String jwt) {
        super();
        setUserName(userName);
        setJwt(jwt);
    }

	public String getUserName() {
		return userName;
	}
	
	public String getJwt() {
		return jwt;
	}

	public void setUserName(String userName) {
   		this.userName = checkNotNull(userName, "Received a null pointer as userName in UserAuthenticatedDto");
	}
	
	public void setJwt(String jwt) {
   		this.jwt = checkNotNull(jwt, "Received a null pointer as jwt in UserAuthenticatedDto");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jwt == null) ? 0 : jwt.hashCode());
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
		UserAuthenticatedDto other = (UserAuthenticatedDto) obj;
		if (jwt == null) {
			if (other.jwt != null)
				return false;
		} else if (!jwt.equals(other.jwt))
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
		return "UserAuthenticatedDto [userName=" + userName + ", jwt=" + jwt + "]";
	}
	
}
