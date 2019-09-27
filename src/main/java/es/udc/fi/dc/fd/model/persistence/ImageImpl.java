package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import es.udc.fi.dc.fd.model.Image;

@Entity(name = "Image")
@Table(name = "ImageTable")
@BatchSize(size=10)
public class ImageImpl implements Image {

    @Transient
	private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId", nullable = false, unique = true)
    private Long imageId;

    @ManyToOne(optional = false , fetch=FetchType.LAZY )	
	@JoinColumn(name="user", referencedColumnName="id")
    private UserImpl user;

    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "description")
    private String description;

    public ImageImpl() {
        super();
    }
    
	public ImageImpl(UserImpl user, byte[] data, String description) {
		super();
		setUser(user);
		setData(data);
		setDescription(description);
	}
	
	public ImageImpl(byte[] data, String description) {
		super();
		setData(data);
		setDescription(description);
	}

	@Override
	public Long getImageId() {
		return imageId;
	}
	
	@Override
	public UserImpl getUser() {
		return user;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setImageId(Long imageId) {
		this.imageId = checkNotNull(imageId, "Received a null pointer as imageId in ImageImpl");
	}
	
	@Override
	public void setUser(UserImpl user) {
		this.user = checkNotNull(user, "Received a null pointer as user in ImageImpl");
	}

	@Override
	public void setData(byte[] data) {
		this.data = checkNotNull(data, "Received a null pointer as data in ImageImpl");
	}

	@Override
	public void setDescription(String description) {
		this.description = checkNotNull(description, "Received a null pointer as description in ImageImpl");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ImageImpl other = (ImageImpl) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageImpl [imageId=" + imageId + ", user=" + user + ", image=" + Arrays.toString(data)
				+ ", description=" + description + "]";
	}
	
}
