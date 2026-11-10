package com.Tabetomo.ShopEdit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopEditRepository extends JpaRepository<ShopEditEntity, Long> {

    // 承認待ち一覧
    List<ShopEditEntity> findByStatusOrderByRequestedAtDesc(String status);

    // 承認待ち件数
    long countByStatus(String status);

    // 店舗ごとの編集申請一覧
    List<ShopEditEntity> findByShopIdOrderByRequestedAtDesc(Long shopId);

    // 申請者ごとの編集申請一覧
    List<ShopEditEntity> findByRequesterIdOrderByRequestedAtDesc(Long requesterId);

}