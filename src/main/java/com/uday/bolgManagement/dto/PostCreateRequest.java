package com.uday.bolgManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest (
    @NotBlank(message = "Title is required")
    @Size(min = 5 , max = 150,message = "Title must be between 5 and 150 characters")
    String title,

    @NotBlank(message = "Content cannot be blank")
    String content,

    @NotNull(message = "Author id is required")
    Long authorid
)
{}
