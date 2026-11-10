package com.Tabetomo.shop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    List<ShopEntity> findByStatusOrderByCreatedAtDesc(String status);

    List<ShopEntity> findByGenre(String genre);
    List<ShopEntity> findByNameContainingIgnoreCase(String keyword);
    List<ShopEntity> findByNameContainingIgnoreCaseAndGenre(String keyword, String genre);
    Optional<ShopEntity> findByName(String name);
    Optional<ShopEntity> findById(Long id);

    List<ShopEntity> findByNameContainingIgnoreCaseOrAreaContainingIgnoreCase(String name, String area);
    List<ShopEntity> findByStatus(String status);

    List<ShopEntity> findByStatusAndGenre(String status, String genre);

    List<ShopEntity> findByStatusAndArea(String status, String area);

    List<ShopEntity> findByStatusAndNameContaining(String status, String keyword);
    @Query("""
    		SELECT DISTINCT s
    		FROM ShopEntity s
    		LEFT JOIN FETCH s.reviews
    		WHERE s.status = 'APPROVED'
    		""")
    		List<ShopEntity> findApprovedWithReviews();
    List<ShopEntity> findByStatusAndDeletedFalse(String status);
    List<ShopEntity> findByDeletedTrue();
    List<ShopEntity> findByDeletedTrueAndDeletedAtBefore(LocalDateTime date);
    List<ShopEntity> findByDeletedFalse();


}
