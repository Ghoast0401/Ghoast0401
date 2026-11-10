package com.Tabetomo.DeletionRequest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;

@Controller
public class DeletionRequestController {

    @Autowired
    private DeletionRequestService deletionRequestService;
    @Autowired
    private AccountRepository accountRepository;

    /**
     * shop_detail.html:
     * <form th:action="@{/shop/{id}/delete-request(id=${detail.id})}" method="post">
     */
    @PostMapping("/shop/{id}/delete-request")
    public String requestShopDeletion(
            @PathVariable("id") Long shopId,
            @RequestParam("reason") String reason,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        AccountEntity account = accountRepository.findByUserName(principal.getName())
                .orElseThrow();

        deletionRequestService.requestShopDeletion(shopId, account.getId(), reason);

        redirectAttributes.addFlashAttribute("message",
                "削除申請を送信しました。管理者の承認をお待ちください。");

        return "redirect:/shop/detail/" + shopId;
    }

    /**
     * mypage.html:
     * <form th:action="@{/account/delete-request}" method="post">
     */
    @PostMapping("/account/delete-request")
    public String requestAccountDeletion(
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        AccountEntity account = accountRepository.findByUserName(principal.getName())
                .orElseThrow();

        deletionRequestService.requestAccountDeletion(account.getId());

        redirectAttributes.addFlashAttribute("message",
                "アカウントの削除申請を送信しました。管理者の承認をお待ちください。");

        return "redirect:/mypage";
    }

    /**
     * admin_dashboard.html の「削除申請承認」タブ:
     * <form th:action="@{/admin/approvals/{id}/approve(id=${approval.id})}" method="post">
     */
    @PostMapping("/admin/approvals/{id}/approve")
    public String approve(@PathVariable("id") Long id) {
        deletionRequestService.approve(id);
        return "redirect:/admin?activeTab=approvals";
    }

    /**
     * admin_dashboard.html の「削除申請承認」タブ:
     * <form th:action="@{/admin/approvals/{id}/reject(id=${approval.id})}" method="post">
     */
    @PostMapping("/admin/approvals/{id}/reject")
    public String reject(@PathVariable("id") Long id) {
        deletionRequestService.reject(id);
        return "redirect:/admin?activeTab=approvals";
    }
}
