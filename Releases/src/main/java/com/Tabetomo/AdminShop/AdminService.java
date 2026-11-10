package com.Tabetomo.AdminShop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Service
public class AdminService {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * 店舗更新
     */
    public void update(Long id, ShopEntity shopForm) {

        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

        shop.setName(shopForm.getName());
        shop.setGenre(shopForm.getGenre());
        shop.setArea(shopForm.getArea());
        shop.setAddress(shopForm.getAddress());
        shop.setMapEmbedUrl(
                com.Tabetomo.shop.ShopService.extractMapEmbedUrl(
                        shopForm.getMapEmbedUrl()));
        shop.setBusinessHours(shopForm.getBusinessHours());

        if (shopForm.getStatus() != null &&
                !shopForm.getStatus().isBlank()) {

            shop.setStatus(shopForm.getStatus());
        }

        shopRepository.save(shop);
    }

    /**
     * 削除されていない店舗一覧
     */
    public List<ShopEntity> findAllActive() {

        return shopRepository.findByDeletedFalse();
    }

    /**
     * ゴミ箱一覧
     */
    public List<ShopEntity> findTrash() {

        return shopRepository.findByDeletedTrue();
    }

    /**
     * 論理削除
     */
    public void deleteShop(Long id) {

        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

        shop.setDeleted(true);
        shop.setDeletedAt(LocalDateTime.now());

        shopRepository.save(shop);
    }

    /**
     * 復元
     */
    public void restoreShop(Long id) {

        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

        shop.setDeleted(false);
        shop.setDeletedAt(null);

        shopRepository.save(shop);
    }

    /**
     * 完全削除
     */
    public void deletePermanent(Long id) {

        shopRepository.deleteById(id);
    }

    /**
     * 1か月経過したゴミ箱を自動削除
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteOldTrash() {

        LocalDateTime limit = LocalDateTime.now().minusMonths(1);

        List<ShopEntity> shops =
                shopRepository.findByDeletedTrueAndDeletedAtBefore(limit);

        shopRepository.deleteAll(shops);
    }

}