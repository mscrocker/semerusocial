package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.Image;
import es.udc.fi.dc.fd.model.User;

@Entity(name = "Image")
@Table(name = "ImageTable")
public class ImageImpl implements Image {

	/**
     * Serialization ID.
     */
    @Transient
	private static final long serialVersionUID = 2L;

    /**
     * Entity's userId.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId", nullable = false, unique = true)
    private Long imageId = -1L;
    
    /**
     * Entity's userId.
     */
	@JoinColumn(name="userId", referencedColumnName="userId")
    private User user = null;

    /**
     * userName of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "image", nullable = false)
    private byte[] image = null;//??????????????????????????

    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "age")
    private int age = -1;
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "sex")
    private String sex = "";
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "city")
    private String city = "";
    
    /**
     * password of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the
     * tests.
     */
    @Column(name = "description")
    private String description = "";
    
    /**
     * Constructs an example entity.
     */
    public ImageImpl() {
        super();
    }
    

	

	public ImageImpl(User user, byte[] image, int age, String sex, String city, String description) {
		super();
		this.user = user;
		this.image = image;
		this.age = age;
		this.sex = sex;
		this.city = city;
		this.description = description;
	}




	@Override
	public Long getImageId() {
		return imageId;
	}
	
	@Override
	public User getUser() {
		return user;
	}

	@Override
	public byte[] getImage() {
		return image;
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
	public String getDescription() {
		return description;
	}

	@Override
	public void setImageId(Long imageId) {
		this.imageId = checkNotNull(imageId, "Received a null pointer as imageId");
	}
	
	@Override
	public void setUser(User user) {
		this.user = checkNotNull(user, "Received a null pointer as user");
	}

	@Override
	public void setImage(byte[] image) {
		this.image = checkNotNull(image, "Received a null pointer as image");
	}

	@Override
	public void setAge(int age) {
		this.age = checkNotNull(age, "Received a null pointer as age");
	}

	@Override
	public void setSex(String sex) {
		this.sex = checkNotNull(sex, "Received a null pointer as age");
	}

	@Override
	public void setCity(String city) {
		this.city = checkNotNull(city, "Received a null pointer as city");
	}

	@Override
	public void setDescription(String description) {
		this.description = checkNotNull(description, "Received a null pointer as description");
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageImpl other = (ImageImpl) obj;
		if (age != other.age)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}


	@Override
	public String toString() {
		return "ImageImpl [imageId=" + imageId + ", image=" + Arrays.toString(image) + ", age=" + age + ", sex=" + sex
				+ ", city=" + city + ", description=" + description + "]";
	}
	
	
	
}
