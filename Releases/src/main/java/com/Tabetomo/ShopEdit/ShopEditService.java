package com.Tabetomo.ShopEdit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShopEditService {

    @Autowired
    private ShopEditRepository shopEditRepository;

    @Autowired
    private ShopRepository shopRepository;

    // 編集申請保存
    public ShopEditEntity save(ShopEditEntity entity) {

        return shopEditRepository.save(entity);
    }

    // 承認待ち一覧
    public List<ShopEditEntity> findPending() {

        return shopEditRepository
                .findByStatusOrderByRequestedAtDesc("PENDING");
    }

    // 承認待ち件数
    public long countPending() {

        return shopEditRepository.countByStatus("PENDING");
    }

    // 編集申請承認
    public void approve(Long id) {

        ShopEditEntity request =
                shopEditRepository.findById(id)
                        .orElseThrow();

        ShopEntity shop =
                shopRepository.findById(request.getShopId())
                        .orElseThrow();

        // 編集内容を反映
        shop.setName(request.getName());
        shop.setArea(request.getArea());
        shop.setAddress(request.getAddress());
        shop.setGenre(request.getGenre());
        shop.setMapEmbedUrl(request.getMapEmbedUrl());
        shop.setBusinessHours(request.getBusinessHours());

        shopRepository.save(shop);

        // 承認済みに変更
        request.setStatus("APPROVED");

        shopEditRepository.save(request);
    }

    // 編集申請却下
    public void reject(Long id) {

        ShopEditEntity request =
                shopEditRepository.findById(id)
                        .orElseThrow();

        request.setStatus("REJECTED");

        shopEditRepository.save(request);
    }

}