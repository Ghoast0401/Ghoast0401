package com.Tabetomo.Mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.Review.ReviewEntity;
import com.Tabetomo.Review.ReviewRepository;

@Service
public class MypageService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    // ユーザー取得
    public AccountEntity getUserById(Long userId) {
        return accountRepository.findById(userId).orElse(null);
    }

    // 自分のレビュー一覧取得
    public List<ReviewEntity> getMyReview(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public ReviewEntity getReviewByUserId(Long userId) {

        return reviewRepository.findByUserId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));

    }

    // 指定されたレビューIDの1件を取得
    public ReviewEntity getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));
    }

    // レビュー削除（本人のレビューのみ削除可能）
    public void deleteReview(Long reviewId, Long userId) {

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));

        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("このレビューを削除する権限がありません");
        }

        reviewRepository.delete(review);
    }

    // レビュー更新
    public void updateReview(Long reviewId, Integer rating, String comment) {

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));

        review.setRating(rating);
        review.setComment(comment);

        // 編集後は再承認
        review.setStatus("PENDING");

        reviewRepository.save(review);
    }
}