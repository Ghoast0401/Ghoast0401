package com.Tabetomo.DeletionRequest;

import java.time.LocalDateTime;

// admin_dashboard.html の approvals テーブルが参照する詰め替え用DTO。
// DeletionRequestEntity自体にはtargetIdしか無いため、
// 画面表示用に店舗名／レビュー内容／ユーザー名を解決したラベルを持たせる。
public class DeletionRequestDto {

    private final Long id;
    private final String targetType; // "SHOP", "REVIEW", "ACCOUNT"
    private final String targetLabel;
    private final String reason;
    private final LocalDateTime requestedAt;

    public DeletionRequestDto(Long id, String targetType, String targetLabel, String reason,
            LocalDateTime requestedAt) {
        this.id = id;
        this.targetType = targetType;
        this.targetLabel = targetLabel;
        this.reason = reason;
        this.requestedAt = requestedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }
}
