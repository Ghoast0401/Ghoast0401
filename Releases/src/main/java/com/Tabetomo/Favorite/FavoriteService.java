package com.Tabetomo.Favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public boolean isFavorite(Long userId, Long shopId) {
        if (userId == null || shopId == null) {
            return false;
        }
        return favoriteRepository.existsByUserIdAndShopId(userId, shopId);
    }

    // 既にお気に入り済みなら何もしない（連打などによる重複登録を防ぐ）
    @Transactional
    public void addFavorite(Long userId, Long shopId) {
        if (favoriteRepository.existsByUserIdAndShopId(userId, shopId)) {
            return;
        }
        FavoriteEntity entity = new FavoriteEntity();
        entity.setUserId(userId);
        entity.setShopId(shopId);
        favoriteRepository.save(entity);
    }

    @Transactional
    public void removeFavorite(Long userId, Long shopId) {
        favoriteRepository.deleteByUserIdAndShopId(userId, shopId);
    }

    // 一覧画面などで複数店舗の「お気に入り済みか」をまとめて取得したい場合用
    public List<FavoriteEntity> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
