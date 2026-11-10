package com.Tabetomo.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    List<PhotoEntity> findByShopId(Long shopId);

    @Transactional
    void deleteByShopId(Long shopId);
}