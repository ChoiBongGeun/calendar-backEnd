package com.example.calendar.store;

import com.example.calendar.domain.Todo;
import com.example.calendar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserAndDueDate(User user, LocalDate dueDate);
    List<Todo> findByUserAndDueDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Todo> findByUserAndCompleted(User user, boolean completed);
    List<Todo> findByUser(User user);
} 