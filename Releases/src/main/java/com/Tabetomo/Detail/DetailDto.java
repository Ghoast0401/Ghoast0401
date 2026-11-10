package com.Tabetomo.Detail;

import java.util.ArrayList;
import java.util.List;

public class DetailDto {

	// 店舗情報
	private Long id;
	private String name;
	private String area;
	private String address;
	private String genre;
	private String mapEmbedUrl;

	// 営業時間
	private String businessHours;

	// 店舗を登録したユーザーのID（削除申請ボタンの表示可否判定に使用）
	private Long createdByUserId;

	// ★ お気に入り状態を表すフィールドを追加
	private Boolean isFavorite = false;

	// 予算（任意項目）
	private Integer budget;

	// 店舗の写真URL一覧
	private List<String> photoUrls = new ArrayList<>();

	// レビュー一覧
	private List<ReviewDto> reviews = new ArrayList<>();

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public DetailDto() {
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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

	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	// ★ isFavorite の Getter / Setter を追加
	public Boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public List<ReviewDto> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewDto> reviews) {
		this.reviews = reviews;
	}
}