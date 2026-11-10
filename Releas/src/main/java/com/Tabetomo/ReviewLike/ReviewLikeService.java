package com.Tabetomo.ReviewLike;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewLikeService {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    public boolean isLiked(Long userId, Long reviewId) {
        if (userId == null || reviewId == null) {
            return false;
        }
        return reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId);
    }

    public long countLikes(Long reviewId) {
        return reviewLikeRepository.countByReviewId(reviewId);
    }

    // 既にいいね済みなら何もしない（連打などによる重複登録を防ぐ）
    @Transactional
    public void like(Long userId, Long reviewId) {
        if (reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            return;
        }
        ReviewLikeEntity entity = new ReviewLikeEntity();
        entity.setUserId(userId);
        entity.setReviewId(reviewId);
        reviewLikeRepository.save(entity);
    }

    @Transactional
    public void unlike(Long userId, Long reviewId) {
        reviewLikeRepository.deleteByUserIdAndReviewId(userId, reviewId);
    }

    // 複数レビューの「いいね数」をまとめて取得したい場合用
    public List<ReviewLikeEntity> findByReviewIds(List<Long> reviewIds) {
        return reviewLikeRepository.findByReviewIdIn(reviewIds);
    }
}
