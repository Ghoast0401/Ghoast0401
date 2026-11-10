package com.Tabetomo.ShopEdit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "shop_edit_requests")
public class ShopEditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 編集対象の店舗
    @Column(nullable = false)
    private Long shopId;

    // 申請者
    @Column(nullable = false)
    private Long requesterId;

    // 編集後の内容
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String mapEmbedUrl;

    private String businessHours;

    // PENDING / APPROVED / REJECTED
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @PrePersist
    public void prePersist() {
        requestedAt = LocalDateTime.now();
        status = "PENDING";
    }

    // ---------- getter ----------

    public Long getId() {
        return id;
    }

    public Long getShopId() {
        return shopId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getGenre() {
        return genre;
    }

    public String getMapEmbedUrl() {
        return mapEmbedUrl;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    // ---------- setter ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

}