package com.Tabetomo.Review;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@SuppressWarnings("deprecation")
public class ReviewForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shopName;

    @NotNull(message = "評価を選択してください")
    @Min(value = 1, message = "評価は1〜5で選択してください")
    @Max(value = 5, message = "評価は1〜5で選択してください")
    private Integer rating;


    @Size(max = 1000, message = "コメントは1000文字以内で入力してください")
    private String comment;

	private Long shopId;
	
	private Long reviewId;

	// アップロードされた写真（実際の保存はセッション上のPendingPhotoを使って行う）
	private List<MultipartFile> photos;

	public Long getReviewId() {
	    return reviewId;
	}

	public void setReviewId(Long reviewId) {
	    this.reviewId = reviewId;
	}


	public Long getShopId() {
	    return shopId;
	}

	public String getShopName() {
	    return shopName;
	}

	public Integer getRating() {
	    return rating;
	}

	public String getComment() {
	    return comment;
	}
	    public void setShopId(Long shopId) {
	        this.shopId = shopId;
	    }
	public void setShopName(String shopName) {
		this.shopName=shopName;
	}


	public void setRating(Integer rating) {
		this.rating=rating;
	}

	public void setComment(String comment) {
		this.comment=comment;
	}

	public List<MultipartFile> getPhotos() {
		return photos;
	}

	public void setPhotos(List<MultipartFile> photos) {
		this.photos = photos;
	}

}