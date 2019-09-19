package es.udc.fi.dc.fd.model;

import java.io.Serializable;

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
    public byte[] getImage();
    
    /**
     * Returns the name of the user.
     *
     * @return the user's password
     */
    public int getAge();
    
    /**
     * Returns the name of the user.
     *
     * @return the user's password
     */
    public String getSex();
    
    /**
     * Returns the name of the user.
     *
     * @return the user's password
     */
    public String getCity();
    
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
    public void setUser(final User user);

    /**
     * Changes the name of the entity.
     *
     * @param image
     *            the name to set on the user
     */
    public void setImage(final byte[] image);
    
    /**
     * Changes the name of the entity.
     *
     * @param age
     *            the name to set on the user
     */
    public void setAge(final int age);
    
    /**
     * Changes the name of the entity.
     *
     * @param sex
     *            the name to set on the user
     */
    public void setSex(final String sex);
    
    /**
     * Changes the name of the entity.
     *
     * @param city
     *            the name to set on the user
     */
    public void setCity(final String city);
    
    /**
     * Changes the name of the entity.
     *
     * @param description
     *            the name to set on the user
     */
    public void setDescription(final String description);
}
