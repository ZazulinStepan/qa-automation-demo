package com.example.qa_automation_demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserDatabaseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void shouldCreateUserAndVerifyInDatabase() {

        String email = "db-api-" + System.currentTimeMillis() + "@example.com";

        Integer userId =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                    "name": "DB Test User",
                                    "email": "%s"
                                }
                                """.formatted(email))
                .when()
                        .post("/users")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        var user = jdbcTemplate.queryForMap(
                "SELECT id, name, email FROM users WHERE id = ?",
                userId
        );

        assertEquals(userId, ((Number) user.get("id")).intValue());
        assertEquals("DB Test User", user.get("name"));
        assertEquals(email, user.get("email"));
    }
}