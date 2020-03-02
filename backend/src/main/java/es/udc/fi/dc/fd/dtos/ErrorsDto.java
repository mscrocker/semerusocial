package es.udc.fi.dc.fd.dtos;

import java.util.List;

public class ErrorsDto {

  private String globalError;

  private List<FieldErrorDto> fieldErrors;

  public ErrorsDto() {
    super();
  }

  public ErrorsDto(String globalError) {
    this.globalError = globalError;
  }

  /**
   * Constructor of the errors Dto when we have many validation field errors.
   * @param fieldErrors The list of the validation field errors
   */
  public ErrorsDto(List<FieldErrorDto> fieldErrors) {

    this.fieldErrors = fieldErrors;

  }

  public String getGlobalError() {
    return globalError;
  }

  public void setGlobalError(String globalError) {
    this.globalError = globalError;
  }

  public List<FieldErrorDto> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

}
