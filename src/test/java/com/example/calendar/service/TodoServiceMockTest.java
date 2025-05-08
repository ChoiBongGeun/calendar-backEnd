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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceMockTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private UserDetails userDetails;
    private User user;
    private Todo todo;

    @BeforeEach
    void setUp() {
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        user = User.builder()
                .email("test@example.com")
                .build();

        todo = Todo.builder()
                .id(1L)
                .user(user)
                .title("Test Todo")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .completed(false)
                .build();
    }

    @Test
    @DisplayName("Mock을 사용한 할 일 생성 테스트")
    void createTodoWithMock() {
        // given
        TodoRequest request = new TodoRequest();
        request.setTitle("Test Todo");
        request.setDescription("Test Description");
        request.setDueDate(LocalDate.now());
        request.setCompleted(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse response = todoService.createTodo(userDetails, request);

        // then
        assertNotNull(response);
        assertEquals("Test Todo", response.getTitle());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Mock을 사용한 할 일 목록 조회 테스트")
    void getTodosWithMock() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findByUser(any(User.class))).thenReturn(List.of(todo));

        // when
        List<TodoResponse> responses = todoService.getTodos(userDetails);

        // then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test Todo", responses.get(0).getTitle());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(todoRepository, times(1)).findByUser(any(User.class));
    }

    @Test
    @DisplayName("Mock을 사용한 예외 처리 테스트")
    void testExceptionWithMock() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> {
            todoService.getTodo(userDetails, 1L);
        });
    }

    @Test
    @DisplayName("Mock을 사용한 권한 검사 테스트")
    void testAuthorizationWithMock() {
        // given
        User otherUser = User.builder()
                .email("other@example.com")
                .build();

        Todo otherTodo = Todo.builder()
                .id(1L)
                .user(otherUser)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(otherTodo));

        // when & then
        assertThrows(UnauthorizedException.class, () -> {
            todoService.getTodo(userDetails, 1L);
        });
    }
} 