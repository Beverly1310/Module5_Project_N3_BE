package com.ra.model.dto.res;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDetailResponse {
    Long commentDetailId;
    String username;
    String avatar;
    String review;
    LocalDate createdAt;
    boolean status;
    boolean madeByCurrentUser;
    boolean isModerator;

}
