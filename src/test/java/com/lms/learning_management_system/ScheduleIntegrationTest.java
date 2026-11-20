package com.lms.learning_management_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.learning_management_system.domain.dto.ScheduleCreateDto;
import com.lms.learning_management_system.domain.dto.ScheduleDto;
import com.lms.learning_management_system.domain.dto.ScheduleUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Teacher;
import com.lms.learning_management_system.repository.CourseRepository;
import com.lms.learning_management_system.repository.GroupRepository;
import com.lms.learning_management_system.repository.ScheduleRepository;
import com.lms.learning_management_system.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpEntity.EMPTY;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты ScheduleController")
class ScheduleIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private String baseUrl;

    private Long existingCourseId;
    private Long existingGroupId;
    private Long existingTeacherId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/schedules";
        cleanupDatabase();
        setupBaselineData();
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE schedules, courses, groups, teachers RESTART IDENTITY CASCADE");
    }

    private void setupBaselineData() {
        existingCourseId = courseRepository.save(
                Course.builder().name("Базовый курс").description("Описание курса").build()
        ).getId();

        existingGroupId = groupRepository.save(
                Group.builder().name("Базовая группа").build()
        ).getId();

        existingTeacherId = teacherRepository.save(
                Teacher.builder().firstName("Иван").lastName("Иванов").build()
        ).getId();
    }

    private ScheduleCreateDto createScheduleRequest() {
        return new ScheduleCreateDto(
                existingGroupId,
                existingTeacherId,
                existingCourseId,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                LocalDateTime.of(2026, 11, 20, 11, 30)
        );
    }

    @Nested
    @DisplayName("POST /api/v1/schedules")
    class CreateSchedule {

        @Test
        @DisplayName("Должен создать расписание и вернуть 201")
        void shouldCreateSchedule() {
            ScheduleCreateDto request = createScheduleRequest();

            ResponseEntity<ScheduleDto> response = testRestTemplate
                    .postForEntity(baseUrl, request, ScheduleDto.class);

            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().id());
            assertEquals(existingGroupId, response.getBody().groupId());
            assertEquals(existingTeacherId, response.getBody().teacherId());
            assertEquals(existingCourseId, response.getBody().courseId());
        }

        @Test
        @DisplayName("Должен вернуть 400 при отсутствии обязательных полей")
        void shouldReturn400WhenRequiredFieldsAreMissing() {

            testRestTemplate.postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            ScheduleCreateDto invalidRequest = new ScheduleCreateDto(
                    existingGroupId,
                    existingTeacherId,
                    existingCourseId,
                    LocalDateTime.of(2026, 11, 20, 10, 0),
                    null
            );

            ResponseEntity<String> response = testRestTemplate
                    .postForEntity(baseUrl, invalidRequest, String.class);

            assertEquals(BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/schedules")
    class GetAllSchedules {

        @Test
        @DisplayName("Должен вернуть пустой список, когда нет расписаний")
        void shouldReturnEmptyList() throws Exception {

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<ScheduleDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(0, content.size());
        }

        @Test
        @DisplayName("Должен вернуть список расписаний")
        void shouldReturnSchedules() throws Exception {

            testRestTemplate.postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<ScheduleDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/schedules/{id}")
    class GetScheduleById {

        @Test
        @DisplayName("Должен вернуть расписание по ID")
        void shouldReturnScheduleById() {
            ResponseEntity<ScheduleDto> createResponse = testRestTemplate
                    .postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            assertNotNull(createResponse.getBody());

            Long scheduleId = createResponse.getBody().id();

            ResponseEntity<ScheduleDto> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", ScheduleDto.class, scheduleId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(scheduleId, response.getBody().id());
            assertEquals(existingGroupId, response.getBody().groupId());
            assertEquals(existingTeacherId, response.getBody().teacherId());
            assertEquals(existingCourseId, response.getBody().courseId());
        }

        @Test
        @DisplayName("Должен вернуть 404 для несуществующего ID")
        void shouldReturn404ForNonExistentId() {
            ResponseEntity<String> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", String.class, 99_999);
            assertEquals(NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/schedules/group/{groupId}")
    class GetScheduleByGroup {

        @Test
        @DisplayName("Должен вернуть расписания по группе")
        void shouldReturnSchedulesByGroup() throws Exception {

            testRestTemplate.postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<ScheduleDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
            assertEquals(existingGroupId, content.get(0).groupId());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/schedules/teacher/{teacherId}")
    class GetScheduleByTeacher {

        @Test
        @DisplayName("Должен вернуть расписания по преподавателю")
        void shouldReturnSchedulesByTeacher() throws Exception {

            testRestTemplate.postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<ScheduleDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/schedules/{id}")
    class UpdateSchedule {

        @Test
        @DisplayName("Должен обновить расписание")
        void shouldUpdateSchedule() {
            ResponseEntity<ScheduleDto> createResponse = testRestTemplate
                    .postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            assertNotNull(createResponse.getBody());

            Long scheduleId = createResponse.getBody().id();

            LocalDateTime newStartDate = LocalDateTime.of(2026, 12, 1, 14, 0);
            LocalDateTime newEndDate = LocalDateTime.of(2026, 12, 1, 15, 30);

            ScheduleUpdateDto updateRequest = new ScheduleUpdateDto(newStartDate, newEndDate);

            ResponseEntity<ScheduleDto> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    PUT,
                    new HttpEntity<>(updateRequest),
                    ScheduleDto.class,
                    scheduleId
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(newStartDate, response.getBody().startDate());
            assertEquals(newEndDate, response.getBody().endDate());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/schedules/{id}")
    class DeleteSchedule {

        @Test
        @DisplayName("Должен удалить расписание и вернуть 204")
        void shouldDeleteSchedule() {
            ResponseEntity<ScheduleDto> createResponse = testRestTemplate.postForEntity(baseUrl, createScheduleRequest(), ScheduleDto.class);

            assertNotNull(createResponse.getBody());

            Long scheduleId = createResponse.getBody().id();

            ResponseEntity<Void> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    DELETE,
                    EMPTY,
                    Void.class,
                    scheduleId
            );

            assertEquals(NO_CONTENT, response.getStatusCode());
            assertFalse(scheduleRepository.existsById(scheduleId));
        }
    }
}