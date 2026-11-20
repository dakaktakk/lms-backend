package com.lms.learning_management_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.learning_management_system.domain.dto.GroupCreateDto;
import com.lms.learning_management_system.domain.dto.GroupDto;
import com.lms.learning_management_system.domain.dto.GroupUpdateDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты GroupController")
class GroupIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;
    private Long existingGroupId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/groups";
        cleanupDatabase();
        setupBaselineData();
    }

    @AfterEach
    void tearDown() {
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE student_groups, groups, students RESTART IDENTITY CASCADE");
    }

    private void setupBaselineData() {
        existingGroupId = groupRepository.save(
                Group.builder()
                        .name("Базовая группа ИТ-21")
                        .build()
        ).getId();

        studentRepository.save(
                Student.builder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .build()
        );
    }

    @Nested
    @DisplayName("POST /api/v1/groups")
    class CreateGroup {

        @Test
        @DisplayName("Должен создать группу и вернуть 201")
        void shouldCreateGroup() {
            GroupCreateDto request = new GroupCreateDto("Новая группа");

            ResponseEntity<GroupDto> response = testRestTemplate.postForEntity(baseUrl, request, GroupDto.class);

            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().id());
            assertEquals("Новая группа", response.getBody().name());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/groups")
    class GetAllGroups {

        @Test
        @DisplayName("Должен вернуть страницу с группами")
        void shouldReturnAllGroups() throws Exception {

            ResponseEntity<JsonNode> response = testRestTemplate.getForEntity(
                    baseUrl + "?page=0&size=10",
                    JsonNode.class
            );

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());

            JsonNode contentNode = response.getBody().get("content");
            assertNotNull(contentNode);
            assertTrue(contentNode.isArray());

            List<GroupDto> content = objectMapper.readValue(
                    contentNode.toString(),
                    new TypeReference<>() {}
            );

            assertEquals(1, content.size());
            assertEquals("Базовая группа ИТ-21", content.get(0).name());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/groups/{id}")
    class GetGroupById {

        @Test
        @DisplayName("Должен вернуть группу по ID")
        void shouldReturnGroupById() {
            ResponseEntity<GroupDto> response = testRestTemplate
                    .getForEntity(baseUrl + "/{id}", GroupDto.class, existingGroupId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(existingGroupId, response.getBody().id());
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
    @DisplayName("PUT /api/v1/groups/{id}")
    class UpdateGroup {

        @Test
        @DisplayName("Должен обновить группу")
        void shouldUpdateGroup() {
            GroupUpdateDto request = new GroupUpdateDto("Обновленное имя");

            ResponseEntity<GroupDto> response = testRestTemplate
                    .exchange(baseUrl + "/{id}", HttpMethod.PUT, new HttpEntity<>(request), GroupDto.class, existingGroupId);

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Обновленное имя", response.getBody().name());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/groups/{id}")
    class DeleteGroup {

        @Test
        @DisplayName("Должен удалить группу и вернуть 204")
        void shouldDeleteGroup() {
            ResponseEntity<Void> response = testRestTemplate
                    .exchange(baseUrl + "/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, existingGroupId);

            assertEquals(NO_CONTENT, response.getStatusCode());

            assertFalse(groupRepository.existsById(existingGroupId));
        }
    }
}