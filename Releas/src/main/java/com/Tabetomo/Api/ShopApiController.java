package com.Tabetomo.Api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.Favorite.FavoriteService;
import com.Tabetomo.shop.ShopRepository;

@RestController
@RequestMapping("/api/shop")
public class ShopApiController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ShopRepository shopRepository;

    @PostMapping("/{shopId}/favorite")
    public ResponseEntity<?> favorite(@PathVariable Long shopId, Principal principal) {

        Long userId = resolveUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "ログインが必要です"));
        }

        if (!shopRepository.existsById(shopId)) {
            return ResponseEntity.notFound().build();
        }

        favoriteService.addFavorite(userId, shopId);

        return ResponseEntity.ok(Map.of("success", true, "favorited", true));
    }

    @PostMapping("/{shopId}/unfavorite")
    public ResponseEntity<?> unfavorite(@PathVariable Long shopId, Principal principal) {

        Long userId = resolveUserId(principal);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "ログインが必要です"));
        }

        favoriteService.removeFavorite(userId, shopId);

        return ResponseEntity.ok(Map.of("success", true, "favorited", false));
    }

    // ログイン中のユーザーIDを取得する（未ログインならnull）
    private Long resolveUserId(Principal principal) {
        if (principal == null) {
            return null;
        }
        AccountEntity account = accountRepository.findByUserName(principal.getName()).orElse(null);
        return account != null ? account.getId() : null;
    }
}
