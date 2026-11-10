package com.Tabetomo.DeletionRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletionRequestRepository extends JpaRepository<DeletionRequestEntity, Long> {

    // admin_dashboard.html の削除申請承認タブ用
    List<DeletionRequestEntity> findByStatusOrderByRequestedAtDesc(String status);

    // 同じユーザーが同じ対象に対して重複申請するのを防ぐ
    boolean existsByTargetTypeAndTargetIdAndRequesterIdAndStatus(
            String targetType, Long targetId, Long requesterId, String status);
}
