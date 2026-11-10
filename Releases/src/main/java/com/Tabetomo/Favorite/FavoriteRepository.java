package com.Tabetomo.Favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    boolean existsByUserIdAndShopId(Long userId, Long shopId);

    Optional<FavoriteEntity> findByUserIdAndShopId(Long userId, Long shopId);

    List<FavoriteEntity> findByUserId(Long userId);

    List<FavoriteEntity> findByShopIdIn(List<Long> shopIds);

    @Transactional
    void deleteByUserIdAndShopId(Long userId, Long shopId);

    // 店舗が削除された場合に、そのお気に入りも一緒に消せるように
    @Transactional
    void deleteByShopId(Long shopId);
}
