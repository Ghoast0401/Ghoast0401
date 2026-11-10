package com.Tabetomo.DeletionRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.AdminShop.AdminService;
import com.Tabetomo.Review.ReviewRepository;
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Service
public class DeletionRequestService {

    public static final String TYPE_SHOP = "SHOP";
    public static final String TYPE_REVIEW = "REVIEW";
    public static final String TYPE_ACCOUNT = "ACCOUNT";

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_REJECTED = "REJECTED";

    @Autowired
    private DeletionRequestRepository deletionRequestRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AdminService adminService;

    /**
     * shop_detail.html の「この店舗の削除を申請する」フォームから呼ばれる。
     */
    public void requestShopDeletion(Long shopId, Long requesterId, String reason) {
        if (isDuplicatePending(TYPE_SHOP, shopId, requesterId)) {
            return;
        }

        DeletionRequestEntity request = new DeletionRequestEntity();
        request.setTargetType(TYPE_SHOP);
        request.setTargetId(shopId);
        request.setRequesterId(requesterId);
        request.setReason(reason);

        deletionRequestRepository.save(request);
    }

    /**
     * mypage.html の「アカウント削除申請」ボタンから呼ばれる。
     */
    public void requestAccountDeletion(Long accountId) {
        if (isDuplicatePending(TYPE_ACCOUNT, accountId, accountId)) {
            return;
        }

        DeletionRequestEntity request = new DeletionRequestEntity();
        request.setTargetType(TYPE_ACCOUNT);
        request.setTargetId(accountId);
        request.setRequesterId(accountId);
        request.setReason("本人からの退会希望");

        deletionRequestRepository.save(request);
    }

    private boolean isDuplicatePending(String targetType, Long targetId, Long requesterId) {
        return deletionRequestRepository.existsByTargetTypeAndTargetIdAndRequesterIdAndStatus(
                targetType, targetId, requesterId, STATUS_PENDING);
    }

    /**
     * admin_dashboard.html の「削除申請承認」タブに表示する一覧。
     */
    public List<DeletionRequestDto> getPendingApprovals() {
        return deletionRequestRepository.findByStatusOrderByRequestedAtDesc(STATUS_PENDING)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private DeletionRequestDto toDto(DeletionRequestEntity request) {
        String label = resolveTargetLabel(request.getTargetType(), request.getTargetId());

        return new DeletionRequestDto(
                request.getId(),
                request.getTargetType(),
                label,
                request.getReason(),
                request.getRequestedAt());
    }

    private String resolveTargetLabel(String targetType, Long targetId) {
        if (TYPE_SHOP.equals(targetType)) {
            return shopRepository.findById(targetId)
                    .map(ShopEntity::getName)
                    .orElse("（既に削除済みの店舗）");
        }

        if (TYPE_REVIEW.equals(targetType)) {
            return reviewRepository.findById(targetId)
                    .map(review -> "レビュー: " + truncate(review.getComment(), 30))
                    .orElse("（既に削除済みのレビュー）");
        }

        if (TYPE_ACCOUNT.equals(targetType)) {
            return accountRepository.findById(targetId)
                    .map(AccountEntity::getUsername)
                    .map(username -> username + " さんのアカウント")
                    .orElse("（既に削除済みのアカウント）");
        }

        return "不明な対象";
    }

    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "...";
    }

    /**
     * 承認：申請種別に応じて実際の削除処理を行う。
     * - SHOP    : 論理削除（ゴミ箱へ。既存のAdminService#deleteShopを再利用）
     * - REVIEW  : 物理削除
     * - ACCOUNT : 物理削除
     */
    @Transactional
    public void approve(Long requestId) {
        DeletionRequestEntity request = deletionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("削除申請が見つかりません"));

        switch (request.getTargetType()) {
            case TYPE_SHOP -> adminService.deleteShop(request.getTargetId());
            case TYPE_REVIEW -> reviewRepository.deleteById(request.getTargetId());
            case TYPE_ACCOUNT -> accountRepository.deleteById(request.getTargetId());
            default -> throw new IllegalStateException("不明な削除申請種別です: " + request.getTargetType());
        }

        request.setStatus(STATUS_APPROVED);
        deletionRequestRepository.save(request);
    }

    /**
     * 却下：実データには触れず、申請のステータスだけ更新する。
     */
    public void reject(Long requestId) {
        DeletionRequestEntity request = deletionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("削除申請が見つかりません"));

        request.setStatus(STATUS_REJECTED);
        deletionRequestRepository.save(request);
    }
}
