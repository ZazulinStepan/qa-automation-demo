package com.example.qa_automation_demo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test ///test
    void shouldGetUserById() {

        User user = new User("Test User", "test@example.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        ResponseStatusException.class,
                        () -> userService.getUserById(999L)
                );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldCreateUser() {

        User user = new User("New User", "new@example.com");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.createUser(user);

        assertEquals("New User", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void shouldGetAllUsers() {

        User user1 = new User("User One", "one@example.com");
        User user2 = new User("User Two", "two@example.com");

        when(userRepository.findAll())
                .thenReturn(java.util.List.of(user1, user2));

        var result = userService.getAllUsers();

        assertEquals(2, result.size());

        assertEquals("User One", result.get(0).getName());
        assertEquals("one@example.com", result.get(0).getEmail());

        assertEquals("User Two", result.get(1).getName());
        assertEquals("two@example.com", result.get(1).getEmail());
    }

    @Test
    void shouldDeleteUser() {

        userService.deleteUser(1L);

        org.mockito.Mockito.verify(userRepository)
                .deleteById(1L);
    }
}