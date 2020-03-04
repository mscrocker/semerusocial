package es.udc.fi.dc.fd.controller.entity;

import es.udc.fi.dc.fd.dtos.ErrorsDto;
import es.udc.fi.dc.fd.dtos.FieldErrorDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonControllerAdvice {

  /**
   * Handler for the MethodArgumentNotValidException.
   *
   * @param exception The instance of the exception to handle
   * @return Dto with the error message
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorsDto handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
        .collect(Collectors.toList());

    return new ErrorsDto(fieldErrors);

  }

}
