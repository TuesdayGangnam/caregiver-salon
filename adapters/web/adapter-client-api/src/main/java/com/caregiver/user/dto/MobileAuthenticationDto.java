package com.caregiver.user.dto;

import com.caregiver.user.port.in.SendAccreditationNumberUsecase;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 휴대폰 인증번호 요청 DTO.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MobileAuthenticationDto {

  /**
   * 휴대폰 인증번호를 발송 요청 객체.
   */
  @Getter
  @NoArgsConstructor
  public static class Request {

    /**
     * 휴대폰 번호.
     */
    @NotBlank
    @Pattern(regexp = "(01[0-9])(\\d{3,4})(\\d{4})")
    private String mobile;

    public Request(String mobile) {
      this.mobile = mobile;
    }

    /**
     * SendAccreditationNumberCommand 객체를 리턴합니다.
     */
    public SendAccreditationNumberUsecase.SendAccreditationNumberCommand generateCommand() {
      return new SendAccreditationNumberUsecase.SendAccreditationNumberCommand(this.getMobile());
    }

  }

}
