package com.ra.service.imp;

import com.ra.model.cons.RoleName;
import com.ra.model.dto.req.CommentRequest;
import com.ra.model.dto.res.CommentDetailResponse;
import com.ra.model.dto.res.CommentResponse;
import com.ra.model.dto.res.CommentSection;
import com.ra.model.entity.Comment;
import com.ra.model.entity.CommentDetail;
import com.ra.repository.CommentDetailRepository;
import com.ra.repository.CommentRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final CommentDetailRepository commentDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private static Long currentUserId() {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetail.getId();
    }

    @Override
    public CommentResponse findByUserAndProduct(Long userId, Long productId) {
        Comment comment=commentRepository.findAllByUserIdAndProductId(userId, productId);
        if (comment!=null) {
            return getCommentResponse(comment);
        };
        return  null;
    }

    @Override
    public void addComment(CommentRequest request) {
        if (commentRepository.existsByUserIdAndProductId(currentUserId(), request.getProductId())) {
            throw new RuntimeException("You already left a comment!");
        } else {
            Comment comment = Comment.builder()
                    .comment(request.getComment())
                    .product(productRepository.findById(request.getProductId()).get())
                    .createdAt(LocalDate.now())
                    .status(true)
                    .user(userRepository.findById(currentUserId()).get()).build();
            commentRepository.save(comment);
        }
    }

    @Override
    public void addCommentDetail(CommentRequest request) {
        CommentDetail commentDetail = CommentDetail.builder()
                .review(request.getComment())
                .comment(commentRepository.findById(request.getCommentId()).get())
                .createdAt(LocalDate.now())
                .status(true)
                .user(userRepository.findById(currentUserId()).get()).build();
        commentDetailRepository.save(commentDetail);
    }

    @Override
    public void updateComment(CommentRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new RuntimeException("Comment does not exist!"));
        comment.setComment(request.getComment());
        commentRepository.save(comment);
    }

    @Override
    public void updateCommentDetail(CommentRequest request) {
        CommentDetail commentDetail = commentDetailRepository.findById(request.getCommentDetailId()).orElseThrow(() -> new RuntimeException("Comment does not exist!"));
        commentDetail.setReview(request.getComment());
        commentDetailRepository.save(commentDetail);
    }

    @Override
    public CommentSection findAllByProduct(Long id) {
        // Implement fetching all comments by product ID if needed
        List<Comment> comments = commentRepository.findAllByProductId(id);
        List<CommentResponse> commentResponses = comments.stream().map(this::getCommentResponse).toList();
        boolean userIsModerator = userRepository.findById(currentUserId()).get().getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ADMIN) || role.getRoleName().equals(RoleName.MANAGER));
        return CommentSection.builder()
                .userCommentExists(commentRepository.existsByUserIdAndProductId(currentUserId(), id))
                .isModerator(userIsModerator)
                .comments(commentResponses).build();
    }

    @Override
    @Transactional
    public void toggleDisplayCommentChain(Long commentId) {
        // Fetch the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id " + commentId));

        // Toggle the status
        comment.setStatus(!comment.isStatus());

        // Save the updated comment
        commentRepository.save(comment);
        commentDetailRepository.toggleCommentDetailDisplayByCommentId(commentId);

    }

    @Override
    public void toggleDisplayComment(Long commentId) {
        // Fetch the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id " + commentId));

        // Toggle the status
        comment.setStatus(!comment.isStatus());

        // Save the updated comment
        commentRepository.save(comment);
    }

    @Override

    public void toggleDisplayCommentDetail(Long commentDetailId) {
        // Fetch the comment detail by ID
        CommentDetail commentDetail = commentDetailRepository.findById(commentDetailId)
                .orElseThrow(() -> new NoSuchElementException("Comment detail not found with id " + commentDetailId));

        // Toggle the status
        commentDetail.setStatus(!commentDetail.isStatus());

        // Save the updated comment detail
        commentDetailRepository.save(commentDetail);
    }

    @Override
    @Transactional
    public void deleteCommentChain(Long commentId) {
        // Fetch the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id " + commentId));

        // Fetch and delete related comment details
        List<CommentDetail> commentDetails = commentDetailRepository.findAllByCommentId(commentId);
        commentDetailRepository.deleteAll(commentDetails);

        // Delete the comment
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentDetail(Long commentDetailId) {
        // Fetch the comment detail by ID
        CommentDetail commentDetail = commentDetailRepository.findById(commentDetailId)
                .orElseThrow(() -> new NoSuchElementException("Comment detail not found with id " + commentDetailId));

        // Delete the comment detail
        commentDetailRepository.delete(commentDetail);
    }

    @Override
    public CommentResponse getCommentResponse(Comment comment) {
        List<CommentDetailResponse> responseList = commentDetailRepository.findAllByCommentId(comment.getId()).stream().map(this::getCommentDetailResponse).toList();
        boolean userIsModerator = userRepository.findById(currentUserId()).get().getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ADMIN) || role.getRoleName().equals(RoleName.MANAGER));
        boolean commentMadeByCurrentUser = comment.getUser().getId().equals(currentUserId());
        // Map the Comment entity to CommentResponse DTO
        return CommentResponse.builder()
                .commentId(comment.getId())
                .avatar(userRepository.findById(comment.getUser().getId()).get().getAvatar())
                .username(userRepository.findById(comment.getUser().getId()).get().getUsername())
                .isModerator(userIsModerator)
                .madeByCurrentUser(commentMadeByCurrentUser)
                .createdAt(comment.getCreatedAt())
                .status(comment.isStatus())
                .responseList(responseList)
                .comment(comment.getComment()).build();
    }

    @Override
    public CommentDetailResponse getCommentDetailResponse(CommentDetail commentDetail) {
        boolean userIsModerator = userRepository.findById(currentUserId()).get().getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ADMIN) || role.getRoleName().equals(RoleName.MANAGER));
        boolean commentMadeByCurrentUser = commentDetail.getUser().getId().equals(currentUserId());
        // Map the CommentDetail entity to CommentDetailResponse DTO
        return CommentDetailResponse.builder()
                .commentDetailId(commentDetail.getId())
                .avatar(userRepository.findById(commentDetail.getUser().getId()).get().getAvatar())
                .createdAt(commentDetail.getCreatedAt())
                .review(commentDetail.getReview())
                .madeByCurrentUser(commentMadeByCurrentUser)
                .status(commentDetail.isStatus())
                .isModerator(userIsModerator)
                .username(userRepository.findById(commentDetail.getUser().getId()).get().getUsername()).build();
    }
}
