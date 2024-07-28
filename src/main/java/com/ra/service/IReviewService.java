package com.ra.service;

import com.ra.model.dto.req.ReviewRequest;
import com.ra.model.dto.res.ReviewResponse;
import com.ra.model.dto.res.ReviewSection;
import com.ra.model.entity.Review;

public interface IReviewService {
    void add(ReviewRequest request);

    void update(ReviewRequest request);

    void delete(Long reviewId);

    ReviewResponse getResponse(Review review);

    ReviewSection getReviewListByProductDetail(Long productDetailId);

    boolean boughtByCurrentUser(Long productDetailId);

    void toggleReviewDisplay(Long reviewId);

    boolean isModerator();

    boolean madeByCurrentUser(Review review);

}
