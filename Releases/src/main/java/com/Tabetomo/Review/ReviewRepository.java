package com.Tabetomo.Review;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStatusOrderByCreatedAtDesc(String status);

    List<ReviewEntity> findByShop_Id(Long Shop_id);
    
    // ログインユーザーのID（userId）に紐づくレビューを全件取得する
    List<ReviewEntity> findByUserId(Long userId);
    // （おまけ）新しい順に並べたい場合
    List<ReviewEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 管理画面のレビュー検索用（紐づく店舗名のあいまい検索）
    List<ReviewEntity> findByShop_NameContainingIgnoreCase(String keyword);
    List<ReviewEntity> findByDeletedFalse();

    List<ReviewEntity> findByDeletedTrue();

    }
 