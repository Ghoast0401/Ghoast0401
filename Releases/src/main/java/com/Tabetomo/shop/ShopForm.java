package com.Tabetomo.shop;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public class ShopForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message="店舗名を入力してください")
    private String name;

    @NotBlank(message="エリアを入力してください")
    private String area;

    @NotBlank(message="住所を入力してください")
    private String address;

    @NotBlank(message="ジャンルを選択してください")
    private String genre;


    // 未入力の場合は自動で店舗名の検索リンク仕様になる想定のため、必須にしない
    private String mapEmbedUrl;

    // 営業時間（任意項目）
    private String businessHours;

    // 予算（任意項目、円単位の目安上限額）
    private Integer budget;

    // 店舗の写真（任意項目）
    private List<MultipartFile> photos;

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getMapEmbedUrl() {
        return mapEmbedUrl;
    }

    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<MultipartFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MultipartFile> photos) {
        this.photos = photos;
    }

}