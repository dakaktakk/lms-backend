package com.lms.learning_management_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.learning_management_system.domain.dto.*;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Student;
import com.lms.learning_management_system.repository.GroupRepository;
import com.lms.learning_management_system.repository.StudentRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты StudentController")
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    private String baseUrl;
    private Long existingStudentId;
    private Long existingGroupId1;
    private Long existingGroupId2;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/students";
        cleanupDatabase();
        setupBaselineData();
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE students, groups RESTART IDENTITY CASCADE");
    }

    private void setupBaselineData() {

        existingGroupId1 = groupRepository.save(
                Group.builder().name("Группа ИТ-21").build()
        ).getId();

        existingGroupId2 = groupRepository.save(
                Group.builder().name("Группа ИТ-22").build()
        ).getId();

        existingStudentId = studentRepository.save(
                Student.builder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .build()
        ).getId();
    }

    @Nested
    @DisplayName("POST /api/v1/students")
    class CreateStudent {

        @Test
        @DisplayName("Должен создать студента и вернуть 201")
        void shouldCreateStudent() {

            StudentCreateDto request = new StudentCreateDto(
                    "Алексей",
                    "Кузнецов",
                    Set.of(existingGroupId1, existingGroupId2)
            );

            ResponseEntity<StudentDto> response = testRestTemplate.postForEntity(baseUrl, request, StudentDto.class);

            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().id());
            assertEquals("Алексей", response.getBody().firstName());
            assertEquals("Кузнецов", response.getBody().lastName());
        }

        @Test
        @DisplayName("Должен вернуть 500 при пустом имени")
        void shouldReturn500WhenNameIsEmpty() {
            StudentCreateDto request = new StudentCreateDto(
                    "",
                    "Иванов",
                    Set.of(existingGroupId1)
            );

            ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, request, String.class);

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/students")
    class GetAllStudents {

        @Test
        @DisplayName("Должен вернуть страницу со студентами")
        void shouldReturnAllStudents() throws Exception {

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<StudentDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );


            assertEquals(1, content.size());
            assertEquals("Иван", content.get(0).firstName());
            assertEquals("Иванов", content.get(0).lastName());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/students/{id}")
    class GetStudentById {

        @Test
        @DisplayName("Должен вернуть студента по ID")
        void shouldReturnStudentById() {
            ResponseEntity<StudentDto> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", StudentDto.class, existingStudentId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(existingStudentId, response.getBody().id());
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
    @DisplayName("PUT /api/v1/students/{id}")
    class UpdateStudent {

        @Test
        @DisplayName("Должен обновить студента")
        void shouldUpdateStudent() {

            StudentUpdateDto request = new StudentUpdateDto(
                    "ОбновленноеИмя",
                    "ОбновленнаяФамилия",
                    Set.of(existingGroupId1)
            );

            ResponseEntity<StudentDto> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    StudentDto.class,
                    existingStudentId
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("ОбновленноеИмя", response.getBody().firstName());
            assertEquals("ОбновленнаяФамилия", response.getBody().lastName());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/students/{id}")
    class DeleteStudent {

        @Test
        @DisplayName("Должен удалить студента и вернуть 204")
        void shouldDeleteStudent() {
            ResponseEntity<Void> response = testRestTemplate.exchange(
                    baseUrl + "/{id}",
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Void.class,
                    existingStudentId
            );

            assertEquals(NO_CONTENT, response.getStatusCode());
            assertFalse(studentRepository.existsById(existingStudentId));
        }
    }
}