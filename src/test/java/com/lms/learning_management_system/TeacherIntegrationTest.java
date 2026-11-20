package com.lms.learning_management_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.learning_management_system.domain.dto.TeacherCreateDto;
import com.lms.learning_management_system.domain.dto.TeacherDto;
import com.lms.learning_management_system.domain.dto.TeacherUpdateDto;
import com.lms.learning_management_system.domain.entity.Teacher;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты TeacherController")
class TeacherIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    private String baseUrl;
    private Long existingTeacherId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/teachers";
        cleanupDatabase();
        setupBaselineData();
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE teachers RESTART IDENTITY CASCADE");
    }

    private void setupBaselineData() {
        existingTeacherId = teacherRepository.save(
                Teacher.builder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .build()
        ).getId();
    }

    @Nested
    @DisplayName("POST /api/v1/teachers")
    class CreateTeacher {

        @Test
        @DisplayName("Должен создать преподавателя и вернуть 201")
        void shouldCreateTeacher() {
            TeacherCreateDto request = new TeacherCreateDto("Алексей", "Кузнецов");

            ResponseEntity<TeacherDto> response = testRestTemplate.postForEntity(baseUrl, request, TeacherDto.class);

            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().id());
            assertEquals("Алексей", response.getBody().firstName());
            assertEquals("Кузнецов", response.getBody().lastName());
        }

        @Test
        @DisplayName("Должен вернуть 400 при пустом имени")
        void shouldReturn400WhenNameIsEmpty() {
            TeacherCreateDto request = new TeacherCreateDto("", "Иванов");

            ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, request, String.class);

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/teachers")
    class GetAllTeachers {

        @Test
        @DisplayName("Должен вернуть страницу с преподавателями")
        void shouldReturnAllTeachers() throws Exception {

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<TeacherDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
            assertEquals("Иван", content.get(0).firstName());
            assertEquals("Иванов", content.get(0).lastName());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/teachers/{id}")
    class GetTeacherById {

        @Test
        @DisplayName("Должен вернуть преподавателя по ID")
        void shouldReturnTeacherById() {
            ResponseEntity<TeacherDto> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", TeacherDto.class, existingTeacherId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(existingTeacherId, response.getBody().id());
            assertEquals("Иван", response.getBody().firstName());
            assertEquals("Иванов", response.getBody().lastName());
        }

        @Test
        @DisplayName("Должен вернуть 404 для несуществующего ID")
        void shouldReturn404ForNonExistentId() {
            ResponseEntity<String> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", String.class, 99999L);

            assertEquals(NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/teachers/{id}")
    class UpdateTeacher {

        @Test
        @DisplayName("Должен обновить преподавателя")
        void shouldUpdateTeacher() {
            TeacherUpdateDto request = new TeacherUpdateDto("ОбновленноеИмя", "ОбновленнаяФамилия");

            ResponseEntity<TeacherDto> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    TeacherDto.class,
                    existingTeacherId
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("ОбновленноеИмя", response.getBody().firstName());
            assertEquals("ОбновленнаяФамилия", response.getBody().lastName());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/teachers/{id}")
    class DeleteTeacher {

        @Test
        @DisplayName("Должен удалить преподавателя и вернуть 204")
        void shouldDeleteTeacher() {
            ResponseEntity<Void> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Void.class,
                    existingTeacherId
            );

            assertEquals(NO_CONTENT, response.getStatusCode());
            assertFalse(teacherRepository.existsById(existingTeacherId));
        }
    }
}