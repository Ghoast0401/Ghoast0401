package com.Tabetomo.ShopEdit;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;

@Controller
public class ShopEditController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopEditService shopEditService;

    @Autowired
    private AccountRepository accountRepository;

    // 編集申請画面表示
    @GetMapping("/shops/{id}/edit-request")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        ShopEntity shop =
                shopRepository.findById(id)
                        .orElseThrow();

        model.addAttribute("shop", shop);

        return "shop_edit_request";
    }

    // 編集申請送信
    @PostMapping("/shops/{id}/edit-request")
    public String submit(
            @PathVariable Long id,
            ShopEditEntity form,
            Principal principal) {

        AccountEntity account =
                accountRepository
                        .findByUserName(principal.getName())
                        .orElseThrow();

        form.setShopId(id);
        form.setRequesterId(account.getId());

        shopEditService.save(form);

        return "redirect:/detail/" + id;
    }

    // 承認
    @PostMapping("/admin/edit-approvals/{id}/approve")
    public String approve(
            @PathVariable Long id) {

        shopEditService.approve(id);

        return "redirect:/admin#editApprovals";
    }

    // 却下
    @PostMapping("/admin/edit-approvals/{id}/reject")
    public String reject(
            @PathVariable Long id) {

        shopEditService.reject(id);

        return "redirect:/admin#editApprovals";
    }

}
