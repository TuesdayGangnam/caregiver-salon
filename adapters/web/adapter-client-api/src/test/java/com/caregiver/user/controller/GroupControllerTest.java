package com.caregiver.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.caregiver.common.BaseControllerTest;
import com.caregiver.user.dto.GroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(GroupController.class)
@DisplayName("GroupController 클래스는")
class GroupControllerTest extends BaseControllerTest {

  private static final String URL = "/v1/group";

  @Autowired
  private ObjectMapper objectMapper;

  @ParameterizedTest
  @MethodSource("provideInvalidCapacity")
  @DisplayName(
      "GroupDto.CreateRequest createRequest 는 잘못 수용 인원이 주어졌을 경우 http status 422을 리턴한다"
  )
  public void test_01(String groupName, int capacity, String description) throws Exception {

    final GroupDto.CreateRequest createRequest =
        new GroupDto.CreateRequest(groupName, capacity, description);

    final String requestBody = objectMapper.writeValueAsString(createRequest);

    final ResultActions resultActions = getResultActions(requestBody);

    resultActions.andExpect(status().isUnprocessableEntity());
  }

  private static Stream<Arguments> provideInvalidCapacity() {
    return Stream.of(
        Arguments.of("그룹명", 1, ""),
        Arguments.of("그룹명", 11, "")
    );
  }

  @ParameterizedTest
  @MethodSource("provideRightCapacity")
  @DisplayName(
      "GroupDto.CreateRequest createRequest 는 올바른 수용 인원이 주어졌을 경우 http status 201을 리턴한다"
  )
  void test_02(String groupName, int capacity, String description) throws Exception {

    final GroupDto.CreateRequest createRequest =
        new GroupDto.CreateRequest(groupName, capacity, description);

    final String requestBody = objectMapper.writeValueAsString(createRequest);

    final ResultActions resultActions = getResultActions(requestBody);

    resultActions.andExpect(status().isCreated());
  }

  private static Stream<Arguments> provideRightCapacity() {
    return Stream.of(
        Arguments.of("그룹명", 5, ""),
        Arguments.of("그룹명", 10, "")
    );
  }

  private ResultActions getResultActions(String requestBody) throws Exception {
    return mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)
    );
  }
}
