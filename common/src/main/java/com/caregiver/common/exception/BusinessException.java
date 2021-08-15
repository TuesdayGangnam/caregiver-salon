package com.caregiver.common.exception;

import lombok.Getter;

/**
 * 비즈니스 예외.
 *
 * <p>
 * 애플리케이션에서 발생하는 논리적인 예외.
 * </p>
 */
@Getter
public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;

  /**
   * 비즈니스 예외 인스턴스를 생성한다.
   *
   * @param errorCode 에러코드
   */
  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  /**
   * 비즈니스 예외 인스턴스를 생성한다.
   *
   * @param errorCode 에러코드
   * @param message   메시지
   */
  public BusinessException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
