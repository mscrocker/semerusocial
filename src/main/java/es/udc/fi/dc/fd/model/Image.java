package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.UserImpl;

public interface Image extends Serializable {
	/**
     * Returns the identifier assigned to this user.
     * <p>
     * If no identifier has been assigned yet, then the value is expected to be
     * {@code null} or lower than zero.
     *
     * @return the user identifier
     */
    public Long getImageId();
    
    /**
     * Returns the identifier assigned to this user.
     * <p>
     * If no identifier has been assigned yet, then the value is expected to be
     * {@code null} or lower than zero.
     *
     * @return the user identifier
     */
    public User getUser();

    /**
     * Returns the name of the user.
     *
     * @return the user's userName
     */
    public byte[] getData();
    
    
    
    /**
     * Returns the name of the user.
     *
     * @return the user's password
     */
    public String getDescription();

    /**
     * Sets the identifier assigned to this user.
     *
     * @param imageId
     *            the identifier for the user
     */
    public void setImageId(final Long imageId);
    
    /**
     * Sets the identifier assigned to this user.
     *
     * @param user
     *            the identifier for the user
     */
    public void setUser(final UserImpl user);

    /**
     * Changes the name of the entity.
     *
     * @param data
     *            the name to set on the user
     */
    public void setImage(final byte[] data);
    
    /**
     * Changes the name of the entity.
     *
     * @param description
     *            the name to set on the user
     */
    public void setDescription(final String description);
}
