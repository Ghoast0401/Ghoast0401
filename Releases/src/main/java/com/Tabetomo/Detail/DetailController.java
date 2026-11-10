package com.Tabetomo.Detail;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;

@Controller
@RequestMapping("/shop")
public class DetailController {

    @Autowired
    private DetailService detailService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Principal principal) {

        AccountEntity account = null;
        Long currentUserId = null;

        if (principal != null) {
            account = accountRepository.findByUserName(principal.getName()).orElse(null);
            if (account != null) {
                currentUserId = account.getId();
            }
        }

        model.addAttribute("account", account);

        DetailDto dto = detailService.getDetail(id, currentUserId);
        if (dto == null) {
            model.addAttribute("errorMessage", "指定された店舗が見つかりませんでした。");
            return "not_found";
        }
        model.addAttribute("detail", dto);
        return "shop_detail";
    }
}
