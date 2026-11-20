package com.lms.learning_management_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.learning_management_system.domain.dto.CourseCreateDto;
import com.lms.learning_management_system.domain.dto.CourseDto;
import com.lms.learning_management_system.domain.dto.CourseUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.repository.CourseRepository;
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
@DisplayName("Интеграционные тесты CourseController")
class CourseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    private String baseUrl;
    private Long existingCourseId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/courses";
        cleanupDatabase();
        setupBaselineData();
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE courses RESTART IDENTITY CASCADE");
    }

    private void setupBaselineData() {
        existingCourseId = courseRepository.save(
                Course.builder()
                        .name("Базовый курс Математика")
                        .description("Вводный курс по высшей математике")
                        .build()
        ).getId();
    }

    @Nested
    @DisplayName("POST /api/v1/courses")
    class CreateCourse {

        @Test
        @DisplayName("Должен создать курс и вернуть 201")
        void shouldCreateCourse() {
            CourseCreateDto request = new CourseCreateDto("Новый курс Истории", "Мировая история");

            ResponseEntity<CourseDto> response = testRestTemplate.postForEntity(baseUrl, request, CourseDto.class);

            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().id());
            assertEquals("Новый курс Истории", response.getBody().name());
            assertEquals("Мировая история", response.getBody().description());
        }

        @Test
        @DisplayName("Должен вернуть 500 при пустом названии")
        void shouldReturn500WhenNameIsEmpty() {
            CourseCreateDto request = new CourseCreateDto("", "Некорректное описание");

            ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, request, String.class);

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/courses")
    class GetAllCourses {

        @Test
        @DisplayName("Должен вернуть страницу с курсами")
        void shouldReturnAllCourses() throws Exception {

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<CourseDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
            assertEquals("Базовый курс Математика", content.get(0).name());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/courses/{id}")
    class GetCourseById {

        @Test
        @DisplayName("Должен вернуть курс по ID")
        void shouldReturnCourseById() {
            ResponseEntity<CourseDto> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", CourseDto.class, existingCourseId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(existingCourseId, response.getBody().id());
            assertEquals("Базовый курс Математика", response.getBody().name());
        }

        @Test
        @DisplayName("Должен вернуть 404 для несуществующего ID")
        void shouldReturn404ForNonExistentId() {
            ResponseEntity<String> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", String.class, 99999);

            assertEquals(NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/courses/{id}")
    class UpdateCourse {

        @Test
        @DisplayName("Должен обновить курс")
        void shouldUpdateCourse() {
            CourseUpdateDto request = new CourseUpdateDto("Обновленное название", "Обновленное описание");

            ResponseEntity<CourseDto> response = testRestTemplate
                    .exchange(baseUrl + "/{id}", HttpMethod.PUT, new HttpEntity<>(request), CourseDto.class, existingCourseId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Обновленное название", response.getBody().name());
            assertEquals("Обновленное описание", response.getBody().description());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/courses/{id}")
    class DeleteCourse {

        @Test
        @DisplayName("Должен удалить курс и вернуть 204")
        void shouldDeleteCourse() {
            ResponseEntity<Void> response = testRestTemplate
                    .exchange(baseUrl + "/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, existingCourseId);

            assertEquals(NO_CONTENT, response.getStatusCode());

            assertFalse(courseRepository.existsById(existingCourseId));
        }
    }
}