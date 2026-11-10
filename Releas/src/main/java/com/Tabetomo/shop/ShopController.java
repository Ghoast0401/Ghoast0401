package com.Tabetomo.shop;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PhotoService photoService;

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("shopForm", new ShopForm());
        model.addAttribute("genreGroups", GenreGroup.values());
        return "shop_form";

    }

    // ②→③ [投稿する] ボタン押下時(実際のDB保存)
    @PostMapping("/submit")
    public String submit(@Valid @ModelAttribute ShopForm shopForm,
                         BindingResult result,
                         Model model,
                         Principal principal) {

        System.out.println("★★★★★ submit() が呼ばれました ★★★★★");

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error ->
                System.out.println(error.getField() + " : " + error.getDefaultMessage())
            );
            model.addAttribute("genreGroups", GenreGroup.values());
            return "shop_form";
        }

        System.out.println("保存開始");

        // 管理者が「新しい店舗を登録する」から作った場合など、
        // principalがnullになることは無い想定だが念のためnullチェックする
        Long createdByUserId = null;
        if (principal != null) {
            createdByUserId = accountRepository.findByUserName(principal.getName())
                    .map(AccountEntity::getId)
                    .orElse(null);
        }

        ShopEntity savedShop = shopService.save(shopForm, createdByUserId);

        // 写真が選択されていれば保存する
        photoService.saveAll(shopForm.getPhotos(), savedShop);

        System.out.println("保存終了");

        model.addAttribute("message", "店舗の登録が完了しました。");

        return "shop_register_success";
    }

    @GetMapping("/search")
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String genreGroup,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Principal principal) {

        Long currentUserId = null;
        AccountEntity account = null;
        if (principal != null) {
            account = accountRepository
                    .findByUserName(principal.getName())
                    .orElse(null);
            if (account != null) {
                currentUserId = account.getId();
            }
        }

        org.springframework.data.domain.Page<ShopEntity> shopPage =
                shopService.search(keyword, genre, genreGroup, null, sort, page, currentUserId);

        model.addAttribute("shopLists", shopPage.getContent());
        model.addAttribute("shopPage", shopPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        model.addAttribute("genreList", GenreGroup.values());

        if (account != null) {
            model.addAttribute("account", account);
        }

        return "index";
    }

    // 店舗編集(承認)は com.Tabetomo.AdminShop.ShopEdit / ShopUpdate に実装を統一。
    // ここに同じ "/admin/shops/{id}/edit" をマッピングすると
    // Ambiguous mapping で起動できなくなるため重複定義しない。

    @GetMapping("/api/shops/suggest")
    @ResponseBody
    public List<String> suggestShops(@RequestParam("keyword") String keyword) {
        // 検索結果から店舗名（String）だけのリストを作成して返却する
        return shopService.suggestShops(keyword)
                          .stream()
                          .map(ShopEntity::getName)
                          .toList();
    }
}