package com.caregiver.common.exception;

import lombok.Getter;

/**
 * 오류 코드.
 */
@Getter
public enum ErrorCode {
  BAD_REQUEST(400, "서버가 요청 구문을 인식하지 못했습니다."),
  NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
  CONFLICT(409, "서버가 요청을 수행하는 중에 충돌이 발생했습니다."),
  INVALID_INPUT_VALUE(422, "입력 값이 올바르지 않습니다."),
  INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생하여 요청을 수행할 수 없습니다."),
  ;

  /**
   * Http Status Code.
   */
  private final int status;

  /**
   * 예외 메시지.
   */
  private final String message;

  ErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
