package com.example.qa_automation_demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserNotFoundParameterizedTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @ParameterizedTest
    @ValueSource(longs = {999999L, 888888L, 777777L})
    void shouldReturn404ForNonExistentUser(Long userId) {

        given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(404);
    }
}