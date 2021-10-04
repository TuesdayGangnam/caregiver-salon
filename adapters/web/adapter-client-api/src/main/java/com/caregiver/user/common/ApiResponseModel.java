package com.caregiver.user.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * API 공통 응답 클래스.
 */
@Getter
@ToString
public class ApiResponseModel<E> {

  private final boolean success;
  private final String message;
  private final E data;

  /**
   * ApiResponseModel 객체를 생성합니다.
   */
  @Builder
  public ApiResponseModel(final boolean success, final String message, final E data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  /**
   * 기본 ApiResponseModel 객체를 생성합니다.
   */
  public ApiResponseModel() {
    this.success = true;
    this.message = "";
    this.data = null;
  }

}
