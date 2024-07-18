package com.ra.repository;


import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u")
    Page<User> getAll(Pageable pageable);

    Page<User> findAllByFullNameContains(String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update user u set u.status = not (u.status) where u.id = :id",nativeQuery = true)
    void updateQueryChangeStatus(@Param("id") Long id);
}