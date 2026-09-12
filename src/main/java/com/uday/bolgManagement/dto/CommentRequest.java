package com.uday.bolgManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Comment text caanot be blank")
        @Size(max = 1000, message = "Comment must not excee 1000 character")
        String content

        //clients should never submit who they are in the body-the backend infers it from the jwt.

//        @NotNull(message = "Author Id is required")
//        Long authorId
) {}
