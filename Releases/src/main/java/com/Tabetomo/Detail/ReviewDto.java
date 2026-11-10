package com.Tabetomo.Detail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDto {

    // 既存のフィールド...
    private Long id;
    private Long userId;
    private String username;
    private String comment;
    private Integer rating;
    private String status;
    private LocalDateTime createdAt;
    private Boolean isLiked = false;

    // ★ likeCount フィールドを追加（初期値 0）
    private Integer likeCount = 0;

    // レビューに添付された写真のURL一覧
    private List<String> photoUrls = new ArrayList<>();

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    // --- 既存の Getter / Setter ---

    // ★ likeCount の Getter / Setter を追加
    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}
}