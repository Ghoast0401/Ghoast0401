package com.Tabetomo.DeletionRequest;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

// admin_dashboard.html の「削除申請承認」タブが期待するエンティティ。
// targetType ("SHOP" / "REVIEW" / "ACCOUNT") + targetId の組で
// 何の削除を申請しているかを表す。
// 承認されるまでは実データ(shops/reviews/account)には一切手を付けない。
@Entity
@Table(name = "deletion_requests")
public class DeletionRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_type", nullable = false)
    private String targetType; // "SHOP", "REVIEW", "ACCOUNT"

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "status", nullable = false)
    private String status; // "PENDING", "APPROVED", "REJECTED"

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // ---------- getter / setter ----------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
