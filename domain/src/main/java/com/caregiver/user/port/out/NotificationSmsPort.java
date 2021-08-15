package com.caregiver.user.port.out;

import com.caregiver.common.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * notification SMS 발송 서버를 호출하기 위한 Port.
 */
public interface NotificationSmsPort {

  void sendSmsMessage(NotificationSmsPort.NotificationSmsCommand notificationSmsCommand);

  /**
   * SMS 서비스 요청 객체를 생성합니다.
   */
  @Getter
  @EqualsAndHashCode(callSuper = false)
  class NotificationSmsCommand extends SelfValidating<NotificationSmsPort.NotificationSmsCommand> {

    @NotBlank
    private final String message;

    @NotBlank
    @Pattern(regexp = "(01[0-9])(\\d{3,4})(\\d{4})")
    private final String mobile;

    @NotBlank
    private final String countryCode;

    public NotificationSmsCommand(String message, String mobile, String countryCode) {
      this.message = message;
      this.mobile = mobile;
      this.countryCode = countryCode;
      this.validateSelf();
    }

    /**
     * 국가번호와 휴대폰번호를 연결하여 리턴합니다.
     */
    public String concatCountryCodeMobile() {
      return countryCode + mobile;
    }

  }

}
