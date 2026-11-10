package com.Tabetomo.Review;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.shop.PhotoService;
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PhotoService photoService;

    public ReviewEntity save(ReviewForm form, String username) {

        ReviewEntity review = new ReviewEntity();

        ShopEntity shop = shopRepository.findById(form.getShopId())
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

        review.setShop(shop);

        AccountEntity account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        review.setUserId(account.getId());
        review.setRating(form.getRating());
        review.setComment(form.getComment());

        return reviewRepository.save(review);
    }
    public void updateStatus(Long id, String status) {

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));

        review.setStatus(status);

        reviewRepository.save(review);
    }

    public void delete(Long id) {

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow();

        review.setDeleted(true);

        review.setDeletedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public List<ReviewEntity> findAll() {

        return reviewRepository.findByDeletedFalse();
    }
}