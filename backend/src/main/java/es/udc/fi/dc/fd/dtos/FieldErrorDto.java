package es.udc.fi.dc.fd.dtos;

public class FieldErrorDto {

	private String fieldName;
	private String message;

	public FieldErrorDto() {
		super();
	}

	public FieldErrorDto(String fieldName, String message) {

		this.fieldName = fieldName;
		this.message = message;

	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
