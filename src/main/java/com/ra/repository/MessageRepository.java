package com.ra.repository;

import com.ra.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("select count(*) from Message m where m.status=false and m.user.id=:id")
    int countUnreadMessage(Long id);

    @Query("update Message m set m.status=true where m.status=false and m.user.id=:id")
    void setAsRead(Long id);
    List<Message> findAllByUserId(Long userId);

}
