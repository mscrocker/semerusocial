package es.udc.fi.dc.fd.dtos;


import java.io.Serializable;
import java.util.Arrays;

import org.hibernate.validator.constraints.NotEmpty;

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
    * userName field.password
    * <p>
    * This is a required field and can't be empty.
    */
   @NotEmpty
   private byte[] data;

   @NotEmpty
   private String description;
   
   
	/**
    * Constructs a DTO for the example entity form.
    */
   public ImageCreationDto() {
       super();
   }


	public byte[] getData() {
		return data;
	}


	public void setImage(byte[] data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ImageCreationDto(byte[] data, String description) {
		super();
		this.data = data;
		this.description = description;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(data);
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ImageCreationDto [image=" + Arrays.toString(data) + ", description=" + description
				+ "]";
	}
	
	


}

