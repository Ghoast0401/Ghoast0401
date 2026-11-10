package com.Tabetomo.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.Tabetomo.Favorite.FavoriteRepository;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private static final int PAGE_SIZE = 12;

    public com.Tabetomo.shop.ShopEntity save(ShopForm form) {
        return save(form, null);
    }

    public com.Tabetomo.shop.ShopEntity save(ShopForm form, Long createdByUserId) {
        ShopEntity shop = new ShopEntity();
        shop.setName(form.getName());
        shop.setArea(form.getArea());
        shop.setAddress(form.getAddress());
        shop.setGenre(form.getGenre());
        shop.setMapEmbedUrl(extractMapEmbedUrl(form.getMapEmbedUrl()));
        shop.setBusinessHours(form.getBusinessHours());
        shop.setBudget(form.getBudget());
        shop.setCreatedByUserId(createdByUserId);
        return shopRepository.save(shop);
        // status, createdAtは@PrePersistで自動設定される
    }

    // Googleマップの「地図を埋め込む」から <iframe ... src="URL" ...> を
    // そのまま貼り付けられた場合に、src の中身のURLだけを取り出す。
    // すでにURLだけが貼られている場合はそのまま返す。
    public static String extractMapEmbedUrl(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile("src=[\"']([^\"']+)[\"']").matcher(trimmed);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return trimmed;
    }

    public List<ShopEntity> findAll() {
        return shopRepository.findAll();
    }

    // 予測変換用：キーワードが含まれる店舗名を最大5件返す
    public List<ShopEntity> suggestShops(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return shopRepository.findByNameContainingIgnoreCase(keyword);
    }

    public void update(Long id, ShopEntity shopForm) {

        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));

        shop.setName(shopForm.getName());
        shop.setArea(shopForm.getArea());
        shop.setAddress(shopForm.getAddress());
        shop.setGenre(shopForm.getGenre());
        shop.setBusinessHours(shopForm.getBusinessHours());
        shop.setBudget(shopForm.getBudget());
        shop.setMapEmbedUrl(
                extractMapEmbedUrl(shopForm.getMapEmbedUrl()));

        shopRepository.save(shop);
    }

    public ShopEntity findById(Long id) {

        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("店舗が見つかりません"));
    }
  
    public Page<ShopEntity> search(
            String keyword,
            String genre,
            String genreGroup,
            String area,
            String sort,
            int page,
            Long currentUserId   // ログイン中ユーザーのID(未ログインならnull)
    ) {

        List<ShopEntity> shops =
                shopRepository.findByStatusAndDeletedFalse("APPROVED");

        if (keyword != null && !keyword.isBlank()) {
            shops = shops.stream()
                    .filter(s -> s.getName().contains(keyword))
                    .toList();
        }

        if (genre != null && !genre.isBlank()) {
            shops = shops.stream()
                    .filter(s -> genre.equals(s.getGenre())
                            || (s.getGenre() != null && s.getGenre().startsWith(genre + "（")))
                    .toList();
        }

        // 大分類（GenreGroup）での絞り込み
        if (genreGroup != null && !genreGroup.isBlank()) {
            GenreGroup group = null;
            try {
                group = GenreGroup.valueOf(genreGroup);
            } catch (IllegalArgumentException e) {
                // 不正な値が来た場合は絞り込みを行わない
            }
            if (group != null) {
                GenreGroup finalGroup = group;
                shops = shops.stream()
                        .filter(s -> {
                            GenreCategory category = GenreCategory.resolveCategory(s.getGenre());
                            return category != null && finalGroup.getCategories().contains(category);
                        })
                        .toList();
            }
        }

        if (area != null && !area.isBlank()) {
            shops = shops.stream()
                    .filter(s -> area.equals(s.getArea()))
                    .toList();
        }

        if ("rating".equals(sort)) {

            shops = shops.stream()
                    .sorted((a, b) ->
                            Double.compare(
                                    b.getAverageRating(),
                                    a.getAverageRating()
                            ))
                    .toList();

        } else if ("reviewCount".equals(sort)) {

            // レビュー件数が多い順（同数の場合は新着順で並べる）
            shops = shops.stream()
                    .sorted((a, b) -> {
                        int cmp = Integer.compare(
                                b.getReviewCount(),
                                a.getReviewCount());
                        if (cmp != 0) {
                            return cmp;
                        }
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    })
                    .toList();

        } else {

            shops = shops.stream()
                    .sorted((a, b) ->
                            b.getCreatedAt()
                                    .compareTo(a.getCreatedAt()))
                    .toList();
        }

        // お気に入り判定（ログイン中のみ）
        if (currentUserId != null) {
            for (ShopEntity shop : shops) {
                boolean favorited = favoriteRepository
                        .existsByUserIdAndShopId(currentUserId, shop.getId());
                shop.setFavoritedByCurrentUser(favorited);
            }
        }

        return toPage(shops, page);
    }

    private Page<ShopEntity> toPage(List<ShopEntity> shops, int page) {

        int safePage = Math.max(page, 0);

        int total = shops.size();

        int start = Math.min(safePage * PAGE_SIZE, total);

        int end = Math.min(start + PAGE_SIZE, total);

        List<ShopEntity> content =
                shops.subList(start, end);

        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest
                        .of(safePage, PAGE_SIZE);

        return new org.springframework.data.domain.PageImpl<>(
                content,
                pageable,
                total
        );
    }
}