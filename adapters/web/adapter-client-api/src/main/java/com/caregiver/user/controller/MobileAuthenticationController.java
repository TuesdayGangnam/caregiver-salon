package com.caregiver.user.controller;

import com.caregiver.user.common.ApiResponseModel;
import com.caregiver.user.dto.MobileAuthenticationDto;
import com.caregiver.user.port.in.SendAccreditationNumberUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 휴대폰 인증 컨트롤러.
 */
@RestController
@RequestMapping("/v1/mobile")
@RequiredArgsConstructor
public class MobileAuthenticationController {

  private final SendAccreditationNumberUsecase sendAccreditationNumberUsecase;

  /**
   * 휴대폰 인증번호 발송 요청 합니다.
   *
   * @param request 인증번호를 요청하기위한 요청
   */
  @PostMapping("/accreditation-number/accept-sms")
  public ResponseEntity<ApiResponseModel<?>> acceptAccreditationNumber(
      @RequestBody @Valid MobileAuthenticationDto.Request request) {

    sendAccreditationNumberUsecase.send(request.generateCommand());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ApiResponseModel<>());
  }

}
