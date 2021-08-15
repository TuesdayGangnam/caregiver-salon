package com.caregiver.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.caregiver.common.BaseControllerTest;
import com.caregiver.user.dto.MobileAuthenticationDto;
import com.caregiver.user.port.in.SendAccreditationNumberUsecase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MobileAuthenticationController.class)
@DisplayName("MobileAuthenticationController 클래스")
class MobileAuthenticationControllerTest extends BaseControllerTest {

  private static final String URL = "/v1/mobile/accreditation-number/accept-sms";

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private SendAccreditationNumberUsecase sendAccreditationNumberUsecase;

  @ParameterizedTest
  @MethodSource("provideInvalidMobileNumbers")
  @NullSource
  @EmptySource
  @DisplayName(
      "MobileAuthenticationDto.Request 는 잘못된 휴대폰 번호 필드가 주어졌을 경우 http status 422을 리턴한다"
  )
  void test_01(String input) throws Exception {

    final MobileAuthenticationDto.Request request = new MobileAuthenticationDto.Request(input);

    final String requestBody = objectMapper.writeValueAsString(request);

    final ResultActions resultActions = getResultActions(requestBody);

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  private static Stream<Arguments> provideInvalidMobileNumbers() {
    return Stream.of(
        Arguments.of("    "),
        Arguments.of("123lksdnflmk"),
        Arguments.of("010"),
        Arguments.of("010-"),
        Arguments.of("010111"),
        Arguments.of("010zaaa")
    );

  }

  @Test
  @DisplayName(
      "MobileAuthenticationDto.Request 는 올바른 휴대폰 번호 필드가 주어졌을 경우 http status 201을 리턴한다"
  )
  void test_02() throws Exception {

    final String mobileNumber = "01012341234";
    final MobileAuthenticationDto.Request request =
        new MobileAuthenticationDto.Request(mobileNumber);

    final String requestBody = objectMapper.writeValueAsString(request);

    final ResultActions resultActions = getResultActions(requestBody);

    resultActions.andExpect(status().isCreated());
  }

  private ResultActions getResultActions(String requestBody) throws Exception {
    return mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );
  }


}
