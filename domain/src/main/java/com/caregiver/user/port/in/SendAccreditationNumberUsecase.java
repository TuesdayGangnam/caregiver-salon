package com.caregiver.user.port.in;

import com.caregiver.common.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 인증 번호 전송 Usecase.
 */
public interface SendAccreditationNumberUsecase {

  /**
   * 주어진 휴대폰 번호를 SMS 발송서버로 보냅니다.
   */
  void send(SendAccreditationNumberCommand sendAccreditationNumberCommand);

  /**
   * 인증번호 전송 요청 객체.
   */
  @Getter
  @EqualsAndHashCode(callSuper = false)
  class SendAccreditationNumberCommand extends SelfValidating<SendAccreditationNumberCommand> {

    @NotBlank
    @Pattern(regexp = "(01[0-9])(\\d{3,4})(\\d{4})")
    private final String mobile;

    public SendAccreditationNumberCommand(String mobile) {
      this.mobile = mobile;
      this.validateSelf();
    }
  }
}
