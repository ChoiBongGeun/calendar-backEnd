package com.example.calendar.adapter;

import com.example.calendar.domain.Todo;
import com.example.calendar.domain.User;
import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private TodoResponse testTodoResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        testTodoResponse = TodoResponse.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .completed(false)
                .build();
    }

    @Test
    void createTodo_ShouldReturnCreatedTodo() throws Exception {
        TodoRequest request = new TodoRequest();
        request.setTitle("New Todo");
        request.setDescription("New Description");
        request.setDueDate(LocalDate.now());

        Mockito.when(todoService.createTodo(any(), any())).thenReturn(testTodoResponse);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testTodoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(testTodoResponse.getDescription()));
    }

    @Test
    void getTodo_ShouldReturnTodo() throws Exception {
        Mockito.when(todoService.getTodo(any(), any())).thenReturn(testTodoResponse);

        mockMvc.perform(get("/api/todos/{uuid}", testTodoResponse.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testTodoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(testTodoResponse.getDescription()));
    }

    @Test
    void updateTodo_ShouldReturnUpdatedTodo() throws Exception {
        TodoRequest request = new TodoRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setDueDate(LocalDate.now());
        request.setCompleted(true);

        TodoResponse updatedTodoResponse = TodoResponse.builder()
                .id(testTodoResponse.getId())
                .uuid(testTodoResponse.getUuid())
                .title("Updated Title")
                .description("Updated Description")
                .dueDate(LocalDate.now())
                .completed(true)
                .build();

        Mockito.when(todoService.updateTodo(any(), any(), any())).thenReturn(updatedTodoResponse);

        mockMvc.perform(put("/api/todos/{uuid}", testTodoResponse.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTodoResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedTodoResponse.getDescription()))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTodo_ShouldReturnSuccess() throws Exception {
        Mockito.doNothing().when(todoService).deleteTodo(any(), any());

        mockMvc.perform(delete("/api/todos/{uuid}", testTodoResponse.getUuid()))
                .andExpect(status().isOk());
    }

    @Test
    void getTodosByDate_ShouldReturnTodos() throws Exception {
        List<TodoResponse> todos = Arrays.asList(testTodoResponse);
        LocalDate date = LocalDate.now();

        Mockito.when(todoService.getTodosByDate(any(), eq(date))).thenReturn(todos);

        mockMvc.perform(get("/api/todos/date/{date}", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(testTodoResponse.getTitle()))
                .andExpect(jsonPath("$[0].description").value(testTodoResponse.getDescription()));
    }

    @Test
    void getTodosByMonth_ShouldReturnTodos() throws Exception {
        List<TodoResponse> todos = Arrays.asList(testTodoResponse);
        YearMonth yearMonth = YearMonth.of(2024, 4);

        Mockito.when(todoService.getTodosByMonth(any(), eq(yearMonth))).thenReturn(todos);

        mockMvc.perform(get("/api/todos/month/{year}/{month}", yearMonth.getYear(), yearMonth.getMonthValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(testTodoResponse.getTitle()))
                .andExpect(jsonPath("$[0].description").value(testTodoResponse.getDescription()));
    }
}
