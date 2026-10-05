package com.example.qa_automation_demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void shouldCreateUser() {

        String email = "test-" + System.currentTimeMillis() + "@example.com";

        given()
                .contentType("application/json")
                .body("""
                        {
                                "name": "Test User",
                                "email": "%s"
                        }
                        """.formatted(email))
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("name", equalTo("Test User"))
                .body("email", equalTo(email));
    }

    @Test
    void shouldGetUserById() {

        String email = "get-" + System.currentTimeMillis() + "@example.com";

        Integer userId =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                "name": "Get User",
                                "email": "%s"
                                }
                                """.formatted(email))
                .when()
                        .post("/users")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
        .when()
                .get("/users/" + userId)
        .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Get User"))
                .body("email", equalTo(email));
    }

    @Test
    void shouldReturn404WhenUserNotFound() {

        given()
                .when()
                .get("/users/999999")
                .then()
                .statusCode(404);
     }

     @Test
     void shouldGetAllUsers() {

        String email = "list-" + System.currentTimeMillis() + "@example.com";

        given()
                .contentType("application/json")
                .body("""
                        {
                                "name": "List User",
                                "email": "%s"
                        }
                        """.formatted(email))
        .when()
                .post("/users")
        .then()
                .statusCode(201);

        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("email", org.hamcrest.Matchers.hasItem(email));
     }

     @Test
     void shouldDeleteUser() {

        String email = "delete-" + System.currentTimeMillis() + "@example.com";

        Integer userId =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                        "name": "Delete User",
                                        "email": "%s"
                                }
                                """.formatted(email))
                .when()
                        .post("/users")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(404);
     }
}