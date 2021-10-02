package com.caregiver.port.in;

import static com.caregiver.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.amazonaws.services.sns.model.PublishRequest;
import com.caregiver.common.annotation.NotificationAdapter;
import com.caregiver.common.exception.BusinessException;
import com.caregiver.config.AwsSnsClient;
import com.caregiver.user.port.out.NotificationSmsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SMS 문자 메세지를 보냅니다.
 */
@Slf4j
@NotificationAdapter
@RequiredArgsConstructor
public class SendSmsMessageAdapter implements NotificationSmsPort {

  private final AwsSnsClient awsSnsClient;

  /**
   * SMS 문자메세지를 보냅니다.
   *
   * @param command SMS 요청.
   */
  @Override
  public void sendSmsMessage(NotificationSmsCommand command) {
    try {

      awsSnsClient.snsClient()
          .publish(
              new PublishRequest()
                  .withMessage(command.getMessage())
                  .withPhoneNumber(command.concatCountryCodeMobile())
                  .withMessageAttributes(awsSnsClient.getMessageAttributeValue())
          );

    } catch (RuntimeException e) {
      log.error("AWS SMS publish 예외 mobile : {}, exception : {}",
          command.getMobile(), e.getCause());
      throw new BusinessException(INTERNAL_SERVER_ERROR, "SMS 문자 발송에 실패 하였습니다.");
    }
  }

}
