package com.caregiver.sms;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.caregiver.common.BaseConfiguration;
import com.caregiver.config.AwsSnsClient;
import com.caregiver.config.AwsSnsClientFactory;
import com.caregiver.port.in.SendSmsMessageAdapter;
import com.caregiver.user.port.out.NotificationSmsPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 문자메세지 발송 테스트.
 */
@SuppressWarnings({"NonAsciiCharacters", "CheckStyle"})
public class SendSmsMessageAdapterTest extends BaseConfiguration {

  private static final String SMS_MESSAGE = "SMS 메세지";
  private static final String PHONE_NUMBER = "01093793259";
  private static final String COUNTRY_CODE = "+82";

  @Autowired
  SendSmsMessageAdapter sendSmsMessageAdapter;

  @MockBean
  AwsSnsClientFactory awsSnsClientFactory;

  @MockBean
  AwsSnsClient awsSnsClient;

  @MockBean
  AmazonSNS amazonSNS;

  /**
   * 테스트가 실행될 경우 SMS 문자 메세지를 발송합니다.
   */
  @Test
  @DisplayName("aws Sms 메일 발송을 테스트하라")
  public void aws_sms_메일_발송을_테스트하라() {

    //given
    NotificationSmsPort.NotificationSmsCommand command =
        new NotificationSmsPort.NotificationSmsCommand(SMS_MESSAGE, PHONE_NUMBER, COUNTRY_CODE);

    final var publishRequest = new PublishRequest().withMessage(SMS_MESSAGE)
        .withPhoneNumber(command.concatCountryCodeMobile())
        .withMessageAttributes(awsSnsClient.getMessageAttributeValue());

    given(awsSnsClient.snsClient()).willReturn(amazonSNS);

    // when
    sendSmsMessageAdapter.sendSmsMessage(command);

    // then
    verify(awsSnsClient, atLeastOnce()).snsClient();
    verify(amazonSNS, atLeastOnce()).publish(publishRequest);

  }

}
