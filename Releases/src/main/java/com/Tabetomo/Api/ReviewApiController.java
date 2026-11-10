package com.Tabetomo.Api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.Review.ReviewRepository;
import com.Tabetomo.ReviewLike.ReviewLikeService;

@RestController
@RequestMapping("/api/review")
public class ReviewApiController {

    @Autowired
    private ReviewLikeService reviewLikeService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<?> like(@PathVariable Long reviewId, Principal principal) {

        Long userId = resolveUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "ログインが必要です"));
        }

        if (!reviewRepository.existsById(reviewId)) {
            return ResponseEntity.notFound().build();
        }

        reviewLikeService.like(userId, reviewId);

        long likeCount = reviewLikeService.countLikes(reviewId);

        return ResponseEntity.ok(Map.of("success", true, "liked", true, "likeCount", likeCount));
    }

    @PostMapping("/{reviewId}/unlike")
    public ResponseEntity<?> unlike(@PathVariable Long reviewId, Principal principal) {

        Long userId = resolveUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "ログインが必要です"));
        }

        reviewLikeService.unlike(userId, reviewId);

        long likeCount = reviewLikeService.countLikes(reviewId);

        return ResponseEntity.ok(Map.of("success", true, "liked", false, "likeCount", likeCount));
    }

    // ログイン中のユーザーIDを取得する（未ログインならnull）
    private Long resolveUserId(Principal principal) {
        if (principal == null) {
            return null;
        }
        AccountEntity account = accountRepository.findByUserName(principal.getName()).orElse(null);
        return account != null ? account.getId() : null;
    }
}
