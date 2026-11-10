package com.Tabetomo.Detail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.Favorite.FavoriteService;
import com.Tabetomo.Review.ReviewEntity;
import com.Tabetomo.Review.ReviewRepository;
import com.Tabetomo.ReviewLike.ReviewLikeService;
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Service
public class DetailService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ReviewLikeService reviewLikeService;

    @Autowired
    private AccountRepository accountRepository;

    // currentUserId は未ログインの場合 null でよい（その場合は「お気に入り済み/いいね済み」は常にfalse扱い）
    public DetailDto getDetail(Long shopId, Long currentUserId) {

        ShopEntity shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {
            return null;
        }

        // レビュー取得
        List<ReviewEntity> reviewList = reviewRepository.findByShop_Id(shopId);

        List<ReviewDto> dtoList = new ArrayList<>();

        for (ReviewEntity review : reviewList) {

            ReviewDto dto = new ReviewDto();

            dto.setUserId(review.getUserId());
            AccountEntity reviewer = accountRepository.findById(review.getUserId()).orElse(null);
            dto.setUsername(reviewer != null ? reviewer.getUsername() : "退会済みユーザー");
            dto.setId(review.getId());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());

            dto.setLikeCount((int) reviewLikeService.countLikes(review.getId()));
            dto.setIsLiked(reviewLikeService.isLiked(currentUserId, review.getId()));

            List<String> reviewPhotoUrls = new ArrayList<>();
            if (review.getPhotos() != null) {
                for (var photo : review.getPhotos()) {
                    reviewPhotoUrls.add(photo.getUrl());
                }
            }
            dto.setPhotoUrls(reviewPhotoUrls);

            dtoList.add(dto);
        }

        // 店舗情報
        DetailDto detailDto = new DetailDto();

        detailDto.setId(shop.getId());

        detailDto.setName(shop.getName());
        detailDto.setArea(shop.getArea());
        detailDto.setGenre(shop.getGenre());
        detailDto.setAddress(shop.getAddress());
        detailDto.setMapEmbedUrl(shop.getMapEmbedUrl());
        detailDto.setBusinessHours(shop.getBusinessHours());
        detailDto.setBudget(shop.getBudget());
        detailDto.setCreatedByUserId(shop.getCreatedByUserId());

        List<String> shopPhotoUrls = new ArrayList<>();
        if (shop.getPhotos() != null) {
            for (var photo : shop.getPhotos()) {
                shopPhotoUrls.add(photo.getUrl());
            }
        }
        detailDto.setPhotoUrls(shopPhotoUrls);

        detailDto.setIsFavorite(favoriteService.isFavorite(currentUserId, shop.getId()));

        // レビュー一覧
        detailDto.setReviews(dtoList);

        return detailDto;
    }
}
