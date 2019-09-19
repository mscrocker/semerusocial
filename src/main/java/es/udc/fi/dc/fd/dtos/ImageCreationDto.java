package es.udc.fi.dc.fd.dtos;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.model.persistence.UserImpl;

/**
* Represents the form used for the creating and editing example entities.
* <p>
* This is a DTO, meant to allow communication between the view and the
* controller, and mapping all the values from the form. Each of field in the
* DTO matches a field in the form.
* <p>
* Includes Java validation annotations, for applying binding validation. This
* way the controller will make sure it receives all the required data.
*
* @author Bernardo Mart&iacute;nez Garrido
*/
public final class ImageCreationDto implements Serializable {

   /**
    * Serialization ID.
    */
   private static final long serialVersionUID = 1328776989450853492L;

   /**
    * userName field.userName
    * <p>
    * This is a required field and can't be empty.
    */
   @NotEmpty
   private User user;
   
   /**
    * userName field.password
    * <p>
    * This is a required field and can't be empty.
    */
   @NotEmpty
   private byte[] image;
   
   @NotEmpty
   @Size(min= 0,max =100)
   private int age;
   
   @NotEmpty
   private String sex;
   
   @NotEmpty
   private String city;
   
   @NotEmpty
   private String description;
   
   
	/**
    * Constructs a DTO for the example entity form.
    */
   public ImageCreationDto() {
       super();
   }


	public User getUser() {
		return user;
	}


	public void setUser(UserImpl user) {
		this.user = user;
	}


	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ImageCreationDto(User user, byte[] image, int age, String sex, String city, String description) {
		super();
		this.user = user;
		this.image = image;
		this.age = age;
		this.sex = sex;
		this.city = city;
		this.description = description;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
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
		ImageCreationDto other = (ImageCreationDto) obj;
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
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
   


}

