package com.example.calendar.service;

import com.example.calendar.domain.Todo;
import com.example.calendar.domain.User;
import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.exception.NotFoundException;
import com.example.calendar.exception.UnauthorizedException;
import com.example.calendar.store.TodoRepository;
import com.example.calendar.store.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    public TodoResponse createTodo(UserDetails userDetails, TodoRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Todo todo = Todo.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .completed(request.isCompleted())
                .build();

        todo = todoRepository.save(todo);
        return convertToResponse(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodos(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return todoRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodo(UserDetails userDetails, Long id) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));

        if (!todo.getUser().equals(user)) {
            throw new UnauthorizedException("Unauthorized access to todo");
        }

        return convertToResponse(todo);
    }

    @Transactional
    public TodoResponse updateTodo(UserDetails userDetails, Long id, TodoRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));

        if (!todo.getUser().equals(user)) {
            throw new UnauthorizedException("Unauthorized access to todo");
        }

        todo.update(request.getTitle(), request.getDescription(), request.getDueDate(), request.isCompleted());
        return convertToResponse(todo);
    }

    @Transactional
    public void deleteTodo(UserDetails userDetails, Long id) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));

        if (!todo.getUser().equals(user)) {
            throw new UnauthorizedException("Unauthorized access to todo");
        }

        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodosByDate(UserDetails userDetails, LocalDate date) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return todoRepository.findByUserAndDueDate(user, date).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodosByMonth(UserDetails userDetails, YearMonth yearMonth) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return todoRepository.findByUserAndDueDateBetween(user, startDate, endDate).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TodoResponse convertToResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .uuid(todo.getUuid())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .dueDate(todo.getDueDate())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
} 