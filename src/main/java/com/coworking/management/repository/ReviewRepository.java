package com.coworking.management.repository;

import com.coworking.management.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEspaceId(Long espaceId);
    List<Review> findByApproved(boolean approved);
    List<Review> findByEspaceIdAndApproved(Long espaceId, boolean approved);
    boolean existsByUserIdAndEspaceId(Long userId, Long espaceId);
}