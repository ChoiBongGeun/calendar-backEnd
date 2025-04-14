package com.example.calendar.adapter;

import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TodoController todoController;

    private TodoRequest todoRequest;
    private TodoResponse todoResponse;

    @BeforeEach
    void setUp() {
        todoRequest = new TodoRequest();
        todoRequest.setTitle("Test Todo");
        todoRequest.setDescription("Test Description");
        todoRequest.setDueDate(LocalDate.now());
        todoRequest.setCompleted(false);

        todoResponse = TodoResponse.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .completed(false)
                .build();
    }

    @Test
    void createTodo_Success() {
        when(todoService.createTodo(any(UserDetails.class), any(TodoRequest.class)))
                .thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.createTodo(userDetails, todoRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(todoResponse, response.getBody());
    }

    @Test
    void getTodos_Success() {
        when(todoService.getTodos(any(UserDetails.class)))
                .thenReturn(List.of(todoResponse));

        ResponseEntity<List<TodoResponse>> response = todoController.getTodos(userDetails);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<TodoResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(todoResponse, body.get(0));
    }

    @Test
    void getTodo_Success() {
        when(todoService.getTodo(any(UserDetails.class), anyLong()))
                .thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.getTodo(userDetails, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(todoResponse, response.getBody());
    }

    @Test
    void updateTodo_Success() {
        when(todoService.updateTodo(any(UserDetails.class), anyLong(), any(TodoRequest.class)))
                .thenReturn(todoResponse);

        ResponseEntity<TodoResponse> response = todoController.updateTodo(userDetails, 1L, todoRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(todoResponse, response.getBody());
    }

    @Test
    void deleteTodo_Success() {
        ResponseEntity<Void> response = todoController.deleteTodo(userDetails, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getTodosByDate_Success() {
        LocalDate date = LocalDate.now();
        when(todoService.getTodosByDate(any(UserDetails.class), any(LocalDate.class)))
                .thenReturn(List.of(todoResponse));

        ResponseEntity<List<TodoResponse>> response = todoController.getTodosByDate(userDetails, date);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<TodoResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(todoResponse, body.get(0));
    }

    @Test
    void getTodosByMonth_Success() {
        YearMonth yearMonth = YearMonth.now();
        when(todoService.getTodosByMonth(any(UserDetails.class), any(YearMonth.class)))
                .thenReturn(List.of(todoResponse));

        ResponseEntity<List<TodoResponse>> response = todoController.getTodosByMonth(userDetails, yearMonth);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<TodoResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(todoResponse, body.get(0));
    }
}
