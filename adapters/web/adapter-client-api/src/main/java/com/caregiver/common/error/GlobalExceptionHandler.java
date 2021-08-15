package com.caregiver.common.error;

import com.caregiver.common.exception.BusinessException;
import com.caregiver.common.exception.ErrorCode;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

/**
 * 여러 예외 케이스를 핸들링해서 ResponseEntity로 리턴해주는 Handler.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * 입력 값의 제약조건이 위반(ConstraintViolationException)하였을 때 주어진 에러 정보 응답 값을 리턴합니다.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("handleConstraintViolation : {}", ex.getMessage());

    return makeResponseEntity(ex);
  }


  /**
   * 비즈니스 예외가 발생(BusinessException) 하였을 때 주어진 에러 정보 응답 값을 리턴합니다.
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
    log.error("handleBusinessException : {}", ex.getMessage());

    return makeResponseEntity(ex);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    log.error("handleMethodArgumentNotValidException : {}", ex.getMessage());

    final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
    final ApiErrorResponse response = ApiErrorResponse.of(errorCode.getMessage(),
        ex.getBindingResult());

    return makeResponseEntity(errorCode, response);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
                                                       HttpStatus status, WebRequest request) {
    log.error("handleBindException : {}", ex.getMessage());

    final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
    final ApiErrorResponse response = ApiErrorResponse.of(errorCode.getMessage(),
        ex.getBindingResult());

    return makeResponseEntity(errorCode, response);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                           HttpHeaders headers, HttpStatus status,
                                                           WebRequest request) {
    log.error("handleExceptionInternal : {}", ex.getMessage());

    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }

    final ApiErrorResponse response = ApiErrorResponse.of(ex.getMessage());

    return new ResponseEntity<>(response, status);
  }

  private ResponseEntity<Object> makeResponseEntity(ErrorCode errorCode) {
    final ApiErrorResponse response = ApiErrorResponse.of(errorCode.getMessage());
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }

  private ResponseEntity<Object> makeResponseEntity(BusinessException ex) {
    final ErrorCode errorCode = ex.getErrorCode();
    final ApiErrorResponse response = ApiErrorResponse.of(ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }

  private ResponseEntity<Object> makeResponseEntity(ConstraintViolationException ex) {
    final ApiErrorResponse response = ApiErrorResponse.of(
        ErrorCode.INVALID_INPUT_VALUE.getMessage(), ex);
    return new ResponseEntity<>(response, HttpStatus.valueOf(
        ErrorCode.INVALID_INPUT_VALUE.getStatus()));
  }

  private ResponseEntity<Object> makeResponseEntity(ErrorCode errorCode,
                                                    ApiErrorResponse response) {
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }
}
