package com.ra.model.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class ReviewSection {
    private boolean userCanCreateReview;
    private List<ReviewResponse> responseList;
}
