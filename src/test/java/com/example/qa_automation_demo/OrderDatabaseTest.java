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
class OrderDatabaseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void shouldFindOrderWithUserUsingJoin() {

        String email = "join-" + System.currentTimeMillis() + "@example.com";

        Integer userId =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                    "name": "Join User",
                                    "email": "%s"
                                }
                                """.formatted(email))
                .when()
                        .post("/users")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        Integer orderId =
                given()
                        .contentType("application/json")
                        .body("""
                                {
                                    "product": "Laptop",
                                    "amount": 100000,
                                    "user": {
                                        "id": %d
                                    }
                                }
                                """.formatted(userId))
                .when()
                        .post("/orders")
                .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        var result = jdbcTemplate.queryForMap(
                """
                SELECT users.name, users.email, orders.product, orders.amount
                FROM users
                JOIN orders ON users.id = orders.user_id
                WHERE orders.id = ?
                """,
                orderId
        );

        assertEquals("Join User", result.get("name"));
        assertEquals(email, result.get("email"));
        assertEquals("Laptop", result.get("product"));
        assertEquals(100000, ((Number) result.get("amount")).intValue());
    }
}