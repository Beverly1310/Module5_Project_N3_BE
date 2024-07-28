package com.ra.service;

import com.ra.model.dto.req.CommentRequest;
import com.ra.model.dto.res.CommentDetailResponse;
import com.ra.model.dto.res.CommentResponse;
import com.ra.model.dto.res.CommentSection;
import com.ra.model.entity.Comment;
import com.ra.model.entity.CommentDetail;
import org.springframework.data.domain.Page;

public interface ICommentService {
    CommentResponse findByUserAndProduct(Long userId,Long productId);
    void addComment(CommentRequest request);
    void addCommentDetail(CommentRequest request);
    void updateComment(CommentRequest request);
    void updateCommentDetail(CommentRequest request);
    CommentSection findAllByProduct(Long id);

    void toggleDisplayCommentChain(Long commentId);
    void toggleDisplayComment(Long commentId);
    void toggleDisplayCommentDetail(Long commentDetailId);
    void deleteCommentChain(Long commentId);
    void deleteCommentDetail(Long commentDetailId);
    CommentResponse getCommentResponse(Comment comment);
    CommentDetailResponse getCommentDetailResponse(CommentDetail commentDetail);
}
