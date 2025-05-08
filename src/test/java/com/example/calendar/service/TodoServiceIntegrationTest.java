package com.example.calendar.service;

import com.example.calendar.domain.Todo;
import com.example.calendar.domain.User;
import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.store.TodoRepository;
import com.example.calendar.store.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TodoServiceIntegrationTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private UserDetails userDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .build();
        user = userRepository.save(user);
        userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    }

    @Test
    @DisplayName("할 일 생성 및 조회 통합 테스트")
    void createAndGetTodo() {
        // given
        TodoRequest request = new TodoRequest();
        request.setTitle("Test Todo");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());
        request.setCompleted(false);

        // when
        TodoResponse createdTodo = todoService.createTodo(userDetails, request);
        TodoResponse retrievedTodo = todoService.getTodo(userDetails, createdTodo.getId());

        // then
        assertNotNull(createdTodo);
        assertNotNull(retrievedTodo);
        assertEquals(createdTodo.getId(), retrievedTodo.getId());
        assertEquals("Test Todo", retrievedTodo.getTitle());
    }

    @Test
    @DisplayName("할 일 목록 조회 통합 테스트")
    void getTodos() {
        // given
        TodoRequest request1 = new TodoRequest();
        request1.setTitle("Todo 1");
        request1.setDueDate(LocalDate.now());

        TodoRequest request2 = new TodoRequest();
        request2.setTitle("Todo 2");
        request2.setDueDate(LocalDate.now());

        todoService.createTodo(userDetails, request1);
        todoService.createTodo(userDetails, request2);

        // when
        List<TodoResponse> todos = todoService.getTodos(userDetails);

        // then
        assertEquals(2, todos.size());
    }

    @Test
    @DisplayName("할 일 수정 통합 테스트")
    void updateTodo() {
        // given
        TodoRequest createRequest = new TodoRequest();
        createRequest.setTitle("Original Todo");
        createRequest.setDueDate(LocalDate.now());
        TodoResponse createdTodo = todoService.createTodo(userDetails, createRequest);

        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Updated Todo");
        updateRequest.setDueDate(LocalDate.now());
        updateRequest.setCompleted(true);

        // when
        TodoResponse updatedTodo = todoService.updateTodo(userDetails, createdTodo.getId(), updateRequest);

        // then
        assertEquals("Updated Todo", updatedTodo.getTitle());
        assertTrue(updatedTodo.isCompleted());
    }

    @Test
    @DisplayName("할 일 삭제 통합 테스트")
    void deleteTodo() {
        // given
        TodoRequest request = new TodoRequest();
        request.setTitle("Test Todo");
        request.setDueDate(LocalDate.now());
        TodoResponse createdTodo = todoService.createTodo(userDetails, request);

        // when
        todoService.deleteTodo(userDetails, createdTodo.getId());

        // then
        assertThrows(Exception.class, () -> {
            todoService.getTodo(userDetails, createdTodo.getId());
        });
    }
} 