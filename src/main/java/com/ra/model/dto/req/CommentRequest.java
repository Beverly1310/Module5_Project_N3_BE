package com.ra.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
    Long productId;
    Long commentId;
    Long commentDetailId;
    @NotBlank(message = "You cannot post an empty comment!")
    @NotEmpty(message = "You cannot post an empty comment!")
    String comment;
}
