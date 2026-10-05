package com.example.qa_automation_demo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "test@example.com",
            "user@gmail.com",
            "stepan@mail.ru"
    })
    void shouldAcceptValidEmail(String email) {

        assertTrue(email.contains("@"));
    }
}