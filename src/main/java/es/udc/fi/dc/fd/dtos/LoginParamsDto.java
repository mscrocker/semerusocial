package es.udc.fi.dc.fd.dtos;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Objects;

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
public final class LoginParamsDto implements Serializable {

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
   private String userName;
   
   /**
    * userName field.password
    * <p>
    * This is a required field and can't be empty.
    */
   @NotEmpty
   private String password;
   
	/**
    * Constructs a DTO for the example entity form.
    */
   public LoginParamsDto() {
       super();
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

       final UserAuthenticatedDto other = (UserAuthenticatedDto) obj;
       return Objects.equals(userName, other.getUserName());
   }

   /**
    * Returns the value of the userName field.
    * 
    * @return the value of the userName field
    */
   public final String getUserName() {
       return userName;
   }
   
   /**
    * Returns the value of the password field.
    * 
    * @return the value of the password field
    */
   public String getPassword() {
		return password;
	}
   

   @Override
   public final int hashCode() {
       return Objects.hash(userName);
   }

   /**
    * Sets the value of the userName field.
    * 
    * @param userName
    *            the new value for the userName field
    */
   public final void setUserName(final String userName) {
   	this.userName = checkNotNull(userName, "Received a null pointer as userName");
   }
   
   /**
    * Sets the value of the password field.
    * 
    * @param password
    *            the new value for the password field
    */
	public void setPassword(String password) {
		this.password = checkNotNull(password, "Received a null pointer as password");
	}

	@Override
	public String toString() {
		return "UserForm [userName=" + userName + ", password=" + password + "]";
	}

}

