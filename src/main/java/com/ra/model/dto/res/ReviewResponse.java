package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ReviewResponse {
    private Long id;
    private String avatar;
    private String comments;
    private LocalDate createdAt;
    private Integer rating;
    private boolean isModerator;
    private boolean madeByCurrentUser;
    private boolean status;
}
