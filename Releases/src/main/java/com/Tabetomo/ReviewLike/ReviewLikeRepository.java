package com.Tabetomo.ReviewLike;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Long> {

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    Optional<ReviewLikeEntity> findByUserIdAndReviewId(Long userId, Long reviewId);

    List<ReviewLikeEntity> findByUserId(Long userId);

    List<ReviewLikeEntity> findByReviewIdIn(List<Long> reviewIds);

    long countByReviewId(Long reviewId);

    @Transactional
    void deleteByUserIdAndReviewId(Long userId, Long reviewId);

    // レビューが削除された場合に、そのいいねも一緒に消せるように
    @Transactional
    void deleteByReviewId(Long reviewId);
}
