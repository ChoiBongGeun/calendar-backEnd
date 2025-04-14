package com.example.calendar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDate dueDate;
    private boolean completed;
} 