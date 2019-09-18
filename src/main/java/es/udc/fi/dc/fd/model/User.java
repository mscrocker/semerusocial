package es.udc.fi.dc.fd.model;

import java.io.Serializable;

public interface User extends Serializable {
	/**
     * Returns the identifier assigned to this user.
     * <p>
     * If no identifier has been assigned yet, then the value is expected to be
     * {@code null} or lower than zero.
     *
     * @return the user identifier
     */
    public Long getUserId();

    /**
     * Returns the name of the user.
     *
     * @return the user's userName
     */
    public String getUserName();
    
    /**
     * Returns the name of the user.
     *
     * @return the user's password
     */
    public String getPassword();

    /**
     * Sets the identifier assigned to this user.
     *
     * @param userId
     *            the identifier for the user
     */
    public void setUserId(final Long userId);

    /**
     * Changes the name of the entity.
     *
     * @param userName
     *            the name to set on the user
     */
    public void setUserName(final String userName);
    
    
    /**
     * Changes the name of the entity.
     *
     * @param password
     *            the name to set on the user
     */
    public void setPassword(final String password);
}
