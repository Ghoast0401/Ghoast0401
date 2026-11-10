package com.Tabetomo.shop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.Tabetomo.Review.ReviewEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "shops")
public class ShopEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String area;

	@Column(nullable = false)
	private String address;


	@Column(nullable = false)
	private String genre;
	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDateTime createdAt;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String mapEmbedUrl;

	// 営業時間（任意項目）
	private String businessHours;

	@Column(name = "created_by_user_id")
	private Long createdByUserId;

	public Long getCreatedByUserId() {
	    return createdByUserId;
	}
	@OneToMany(mappedBy = "shop",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	private List<PhotoEntity> photos = new ArrayList<>();

	public List<PhotoEntity> getPhotos() {
	    return photos;
	}

	public void setPhotos(List<PhotoEntity> photos) {
	    this.photos = photos;
	}

	// 予算（任意項目、円単位の目安上限額）
	private Integer budget;

	public Integer getBudget() {
	    return budget;
	}

	public void setBudget(Integer budget) {
	    this.budget = budget;
	}

	public void setCreatedByUserId(Long createdByUserId) {
	    this.createdByUserId = createdByUserId;
	}

	@Column(nullable = false, columnDefinition = "boolean default false") 
	private boolean deleted = false;

	private LocalDateTime deletedAt;
	public boolean isDeleted() {
	    return deleted;
	}
	public void setDeleted(boolean deleted) {
	    this.deleted = deleted;
	}

	public LocalDateTime getDeletedAt() {
	    return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
	    this.deletedAt = deletedAt;
	}
    @OneToMany(mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
 private List<ReviewEntity> reviews;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = "APPROVED";
    }
    @Transient
    private boolean favoritedByCurrentUser;

    public boolean isFavoritedByCurrentUser() {
        return favoritedByCurrentUser;
    }
    public void setFavoritedByCurrentUser(boolean favoritedByCurrentUser) {
        this.favoritedByCurrentUser = favoritedByCurrentUser;
    }
    @Transient
    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        // 承認(APPROVED)の仕組みが機能していないため、
        // ステータスを問わず評価が入っているレビューを対象にする
        return reviews.stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
    }
    @Transient
    public int getStarCount() {
        return (int) getAverageRating();
    }


    @Transient
    public int getRoundedRating() {
        return (int) Math.round(getAverageRating());
    }


    @Transient
    public int getReviewCount() {
        if (reviews == null) {
            return 0;
        }
        // getAverageRating() と対象を揃える(APPROVEDのみだと常に0件になっていた)
        return (int) reviews.stream()
                .filter(r -> r.getRating() != null)
                .count();
    }
    // ---------- getter ----------
    public Long getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getName() {
        return name;

    }
	

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<ReviewEntity> getReviews() {
		return reviews;
	}
	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setGenre(String genre) {
		this.genre = genre;
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

	// ---------- setter ----------
	public void setName(String name) {
		this.name = name;
	}

	public void setArea(String area) {
		this.area = area;
	}


public void setStatus(String status) {
    this.status = status;
}
    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

}