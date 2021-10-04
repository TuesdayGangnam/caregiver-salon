package com.caregiver.common.error;

import com.caregiver.user.common.ApiResponseModel;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * API 응답에 에러, 예외를 응답하기 위해 사용하는 Response 객체.
 */
@Getter
public class ApiErrorResponse extends ApiResponseModel<Object> {

  private ApiErrorResponse(String message) {
    super(false, message, null);
  }

  private ApiErrorResponse(String message, Object data) {
    super(false, message, data);
  }

  /**
   * 입력된 메시지와 함께 {sucess : false, data : null} 인 ApiErrorResponse를 리턴합니다.
   *
   * @param message 예외 메세지
   */
  public static ApiErrorResponse of(String message) {
    return new ApiErrorResponse(message);
  }

  /**
   * message, bindingResult의 에러 사유를 포함한 ApiErrorResponse를 리턴합니다.
   *
   * @param message       예외 메세지
   * @param bindingResult 실패한 유효성 검증 결과
   */
  public static ApiErrorResponse of(String message, BindingResult bindingResult) {
    return new ApiErrorResponse(message, FieldErrors.from(bindingResult));
  }

  /**
   * 입력된 메시지, constraintViolationException의 에러 사유를 포함한 ApiErrorResponse를 리턴합니다.
   *
   * @param message                      예외 메세지
   * @param constraintViolationException 유효성 검증 실패
   */
  public static ApiErrorResponse of(String message,
                                    ConstraintViolationException constraintViolationException) {
    return new ApiErrorResponse(message, FieldErrors.from(constraintViolationException));
  }

  /**
   * ex의 이름과 에러코드를 포함한 ApiErrorResponse를 리턴합니다.
   *
   * @param ex 컨트롤러 메서드 인자에 TypeMismatchException이 난 경우
   */
  public static ApiErrorResponse of(MethodArgumentTypeMismatchException ex) {
    final List<FieldError> errors = FieldErrors.of(
        ex.getName(),
        ex.getErrorCode()
    );

    return new ApiErrorResponse(ex.getMessage(), errors);
  }

  /**
   * 에러가 난 필드명과 사유.
   */
  @Getter
  protected static class FieldError {
    private final String field;
    private final String reason;

    @Builder
    public FieldError(String field, String reason) {
      this.field = field;
      this.reason = reason;
    }
  }

  /**
   * FieldError 컬렉션을 반환하는 메서드들을 모은 클래스.
   */
  protected static class FieldErrors {
    private FieldErrors() {
    }

    /**
     * field, reason을 이용하여 생성한 사이즈가 1인 {@code List<FieldError>}를 리턴합니다.
     *
     * @param field  에러가 난 필드명
     * @param reason 사유
     */
    public static List<FieldError> of(final String field, final String reason) {
      return List.of(new FieldError(field, reason));
    }

    /**
     * bindingResult에서 field와 message를 추출하여 생성한 {@code List<FieldError>}를 리턴합니다.
     *
     * @param bindingResult 실패한 유효성 검증 결과
     */
    private static List<FieldError> from(final BindingResult bindingResult) {
      if (Objects.isNull(bindingResult)) {
        return Collections.emptyList();
      }

      final var fieldErrors = bindingResult.getFieldErrors();

      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }

    /**
     * constraintViolationException에서 propertyPath와 message를 추출하여 생성한
     * {@code List<FieldError>}를 리턴합니다.
     *
     * @param constraintViolationException 유효성 검증 실패
     */
    private static List<FieldError> from(
        final ConstraintViolationException constraintViolationException) {
      if (Objects.isNull(constraintViolationException)) {
        return Collections.emptyList();
      }

      Set<ConstraintViolation<?>> constraintViolations =
          constraintViolationException.getConstraintViolations();

      return constraintViolations.stream()
          .map(error -> new FieldError(
              error.getPropertyPath().toString(),
              error.getMessage()))
          .collect(Collectors.toList());
    }
  }
}
