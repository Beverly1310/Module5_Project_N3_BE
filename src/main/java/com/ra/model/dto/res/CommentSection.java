package com.ra.model.dto.res;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentSection {
    List<CommentResponse> comments;
    boolean userCommentExists;
    boolean isModerator;
}
