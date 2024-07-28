package com.ra.model.dto.res;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long commentId;
    String username;
    String avatar;
    String comment;
    LocalDate createdAt;
    boolean status;
    boolean isModerator;
    boolean madeByCurrentUser;
    List<CommentDetailResponse> responseList;
}
