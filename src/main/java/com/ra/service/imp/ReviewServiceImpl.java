package com.ra.service.imp;

import com.ra.model.cons.OrderStatus;
import com.ra.model.cons.RoleName;
import com.ra.model.dto.req.ReviewRequest;
import com.ra.model.dto.res.ReviewResponse;
import com.ra.model.dto.res.ReviewSection;
import com.ra.model.entity.Review;
import com.ra.model.entity.User;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.ProductDetailRepository;
import com.ra.repository.ReviewRepository;
import com.ra.repository.UserRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ReviewRepository reviewRepository;
    private final OrderDetailRepository orderDetailRepository;

    private User currentUser() {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(customUserDetail.getId()).get();
    }

    @Override
    public void add(ReviewRequest request) {
        Review review = Review.builder()
                .comments(request.getComments())
                .user(currentUser())
                .rating(request.getRating())
                .productDetail(productDetailRepository.findById(request.getProductDetailId()).orElseThrow(() -> new RuntimeException("Product not found!"))).build();
        reviewRepository.save(review);
    }

    @Override
    public void update(ReviewRequest request) {
        Review review = reviewRepository.findById(request.getReviewId()).orElseThrow(() -> new RuntimeException("Review not found!"));
        review.setComments(request.getComments());
        review.setRating(request.getRating());
        reviewRepository.save(review);
    }

    @Override
    public void delete(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewResponse getResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .avatar(review.getUser().getAvatar())
                .madeByCurrentUser(madeByCurrentUser(review))
                .createdAt(review.getCreatedAt())
                .rating(review.getRating())
                .isModerator(isModerator())
                .status(review.isStatus())
                .comments(review.getComments()).build();
    }

    @Override
    public ReviewSection getReviewListByProductDetail(Long productDetailId) {
        List<Review> reviewList = reviewRepository.findAllByProductDetailId(productDetailId);
        List<ReviewResponse> responseList = reviewList.stream().map(this::getResponse).toList();

        Long currentUserId = currentUser().getId();
        boolean userReviewExists = reviewRepository.existsByUserIdAndProductDetailId(currentUserId, productDetailId);
        boolean userHasBoughtProduct = boughtByCurrentUser(currentUserId);

        // User can create a review if they have bought the product and have not already reviewed it
        boolean userCanCreateReview = userHasBoughtProduct && !userReviewExists;

        return ReviewSection.builder()
                .responseList(responseList)
                .userCanCreateReview(userCanCreateReview)
                .build();
    }
    @Override
    public boolean boughtByCurrentUser(Long productDetailId) {
        return orderDetailRepository.existsByIdOrderStatusAndIdOrderUserIdAndIdProductDetailId(OrderStatus.DELIVERY, currentUser().getId(), productDetailId);
    }

    @Override
    public void toggleReviewDisplay(Long reviewId) {
        reviewRepository.toggleDisplay(reviewId);
    }

    @Override
    public boolean isModerator() {
        return currentUser().getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ADMIN) || role.getRoleName().equals(RoleName.MANAGER));
    }

    @Override
    public boolean madeByCurrentUser(Review review) {
        return Objects.equals(review.getUser(), currentUser());
    }
}
