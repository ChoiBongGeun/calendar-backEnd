package com.example.calendar.adapter;

import com.example.calendar.dto.TodoRequest;
import com.example.calendar.dto.TodoResponse;
import com.example.calendar.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Tag(name = "할 일", description = "할 일 관리 API")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "할 일 생성", description = "새로운 할 일을 생성합니다.")
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(userDetails, request));
    }

    @Operation(summary = "할 일 목록 조회", description = "사용자의 모든 할 일을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(todoService.getTodos(userDetails));
    }

    @Operation(summary = "할 일 상세 조회", description = "특정 할 일의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodo(userDetails, id));
    }

    @Operation(summary = "할 일 수정", description = "할 일 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(userDetails, id, request));
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        todoService.deleteTodo(userDetails, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일별 할 일 조회", description = "특정 날짜의 할 일을 조회합니다.")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TodoResponse>> getTodosByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd 형식)")
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(todoService.getTodosByDate(userDetails, date));
    }

    @Operation(summary = "월별 할 일 조회", description = "특정 월의 할 일을 조회합니다.")
    @GetMapping("/month/{yearMonth}")
    public ResponseEntity<List<TodoResponse>> getTodosByMonth(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "조회할 연월 (yyyy-MM 형식)")
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return ResponseEntity.ok(todoService.getTodosByMonth(userDetails, yearMonth));
    }
} 