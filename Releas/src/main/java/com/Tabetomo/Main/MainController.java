package com.Tabetomo.Main;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.DeletionRequest.DeletionRequestService;
import com.Tabetomo.Review.ReviewRepository;
import com.Tabetomo.shop.GenreGroup; // Enumを使う場合の想定
import com.Tabetomo.shop.ShopEntity;
import com.Tabetomo.shop.ShopRepository;
import com.Tabetomo.shop.ShopService;

@Controller
public class MainController {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private DeletionRequestService deletionRequestService;

	
	@GetMapping({"/", "/shop/list"})
	public String index(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(name = "keyword", required = false) String keyword,
	        @RequestParam(name = "genre", required = false) String genre,
	        @RequestParam(name = "genreGroup", required = false) String genreGroup,
	        @RequestParam(defaultValue = "new") String sort,
	        Model model,
	        Principal principal) {

	    AccountEntity account = null;

	    if (principal != null) {
	        account = accountRepository.findByUserName(principal.getName()).orElse(null);

	        if (account != null && "ADMIN".equals(account.getRole())) {
	            return "redirect:/admin";
	        }
	    }

	    Long accountId = null;

	    if (account != null) {
	        accountId = account.getId();
	    }

	    Page<ShopEntity> shopPage =
	            shopService.search(
	                    keyword,
	                    genre,
	                    genreGroup,
	                    null,
	                    sort,
	                    page,
	                    accountId);
	    model.addAttribute("shopPage", shopPage);
	    model.addAttribute("shopLists", shopPage.getContent());
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("genre", genre);
	    model.addAttribute("genreList", GenreGroup.values());
	    model.addAttribute("title", "店舗一覧");
	    model.addAttribute("msg", "ログインに成功しました！");

	    if (account != null) {
	        model.addAttribute("account", account);
	    }

	    return "index";
	    }

}