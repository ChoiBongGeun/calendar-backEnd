package com.example.calendar.service;

import com.example.calendar.domain.Todo;
import com.example.calendar.domain.User;
import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.exception.NotFoundException;
import com.example.calendar.exception.UnauthorizedException;
import com.example.calendar.store.TodoRepository;
import com.example.calendar.store.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TodoService todoService;

    private User user;
    private Todo todo;
    private TodoRequest todoRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        todo = Todo.builder()
                .id(1L)
                .user(user)
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .completed(false)
                .build();

        todoRequest = new TodoRequest();
        todoRequest.setTitle("Test Todo");
        todoRequest.setDescription("Test Description");
        todoRequest.setDueDate(LocalDate.now());
        todoRequest.setCompleted(false);

        when(userDetails.getUsername()).thenReturn("test@example.com");
    }

    @Test
    @DisplayName("할 일 생성 테스트")
    void createTodo() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoResponse response = todoService.createTodo(userDetails, todoRequest);

        assertNotNull(response);
        assertEquals(todo.getTitle(), response.getTitle());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("할 일 조회 테스트")
    void getTodo() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));

        TodoResponse response = todoService.getTodo(userDetails, 1L);

        assertNotNull(response);
        assertEquals(todo.getTitle(), response.getTitle());
    }

    @Test
    @DisplayName("할 일 수정 테스트")
    void updateTodo() {
        TodoRequest request = new TodoRequest();
        request.setTitle("Updated Todo");
        request.setDescription("Updated Description");
        request.setDueDate(LocalDate.now());
        request.setCompleted(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));

        TodoResponse response = todoService.updateTodo(userDetails, 1L, request);

        assertNotNull(response);
        assertEquals("Updated Todo", response.getTitle());
        assertTrue(response.isCompleted());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(todoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("할 일 삭제 테스트")
    void deleteTodo() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));

        todoService.deleteTodo(userDetails, 1L);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(todoRepository, times(1)).findById(anyLong());
        verify(todoRepository, times(1)).delete(any(Todo.class));
    }

    @Test
    @DisplayName("할 일 조회 실패 테스트 - 사용자 없음")
    void getTodo_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            todoService.getTodo(userDetails, 1L));
    }

    @Test
    @DisplayName("할 일 조회 실패 테스트 - 권한 없음")
    void getTodo_Unauthorized() {
        User otherUser = User.builder()
                .id(2L)
                .email("other@example.com")
                .password("password")
                .build();

        Todo otherTodo = Todo.builder()
                .id(1L)
                .user(otherUser)
                .title("Other Todo")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(otherTodo));

        assertThrows(UnauthorizedException.class, () -> 
            todoService.getTodo(userDetails, 1L));
    }

    @Test
    void getTodos_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findByUser(user)).thenReturn(List.of(todo));

        List<TodoResponse> responses = todoService.getTodos(userDetails);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(todo.getTitle(), responses.get(0).getTitle());
    }

    @Test
    void getTodosByDate_Success() {
        LocalDate date = LocalDate.now();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findByUserAndDueDate(user, date)).thenReturn(List.of(todo));

        List<TodoResponse> responses = todoService.getTodosByDate(userDetails, date);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(todo.getTitle(), responses.get(0).getTitle());
    }

    @Test
    void getTodosByMonth_Success() {
        YearMonth yearMonth = YearMonth.now();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findByUserAndDueDateBetween(any(User.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(todo));

        List<TodoResponse> responses = todoService.getTodosByMonth(userDetails, yearMonth);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(todo.getTitle(), responses.get(0).getTitle());
    }
} 