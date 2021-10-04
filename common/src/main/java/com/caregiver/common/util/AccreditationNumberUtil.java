package com.caregiver.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 인증번호 유틸 클래스.
 */
public class AccreditationNumberUtil {

  /**
   * origin(포함)과 bound(미 포함) 사이에 분포된 난수를 리턴합니다.
   */
  public static int generate(int origin, int bound) {
    return ThreadLocalRandom.current().nextInt(origin, bound);
  }

}
