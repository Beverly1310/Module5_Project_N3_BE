package com.ra.repository;

import com.ra.model.entity.CommentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDetailRepository extends JpaRepository<CommentDetail,Long> {
    void deleteAllByCommentId(Long commentId);
    List<CommentDetail> findAllByCommentId(Long commentId);
    @Query("UPDATE CommentDetail c SET c.status = CASE WHEN c.status = true THEN false ELSE true END WHERE c.id = :id")
    void toggleCommentDetailDisplay(Long id);

    @Query("UPDATE CommentDetail c SET c.status = CASE WHEN c.status = true THEN false ELSE true END WHERE c.comment.id = :id")
    void toggleCommentDetailDisplayByCommentId(Long id);
}
