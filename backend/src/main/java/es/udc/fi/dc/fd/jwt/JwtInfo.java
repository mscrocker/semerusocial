package es.udc.fi.dc.fd.jwt;

import java.security.Principal;

public class JwtInfo implements Principal {

	private Long userId;
	private String userName;

	public JwtInfo(Long userId, String userName) {

		this.userId = userId;
		this.userName = userName;

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getName() {
		return getUserName();
	}

}
