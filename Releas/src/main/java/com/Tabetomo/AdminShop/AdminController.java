package com.Tabetomo.AdminShop;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.DeletionRequest.DeletionRequestService;
import com.Tabetomo.Review.ReviewService;
import com.Tabetomo.ShopEdit.ShopEditService;
import com.Tabetomo.shop.GenreGroup;
import com.Tabetomo.shop.ShopForm;
import com.Tabetomo.shop.ShopRepository;
@Controller
public class AdminController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AdminService adminService;
	@Autowired
	DeletionRequestService deletionRequestService;
	@Autowired
	ShopRepository shopRepository;
	@Autowired
	ReviewService reviewService;
	@Autowired
	ShopEditService shopEditService;
	@GetMapping("/admin/shops/new")
	public String form(Model model) {
		model.addAttribute("shopForm", new ShopForm());
		model.addAttribute("genreGroups", GenreGroup.values());
		return "shop_form";
	}
	@PostMapping("/admin/shops/{id}/delete")
	public String delete(
	        @PathVariable Long id,
	        Principal principal) {

	    AccountEntity account =
	            accountRepository.findByUserName(principal.getName())
	            .orElseThrow();

	    adminService.deleteShop(id);

	    return "redirect:/admin#shops";
	}
	@GetMapping("/admin/trash")
	public String trash() {
	    // "admin#shops" は存在しないテンプレート名だったため、
	    // 既にtrashedShopListを用意している /admin にリダイレクトする
	    return "redirect:/admin";
	}@PostMapping("/admin/shops/{id}/restore")
	public String restore(@PathVariable Long id) {

	    adminService.restoreShop(id);

	    return "redirect:/admin#shops";
	}@PostMapping("/admin/shops/{id}/delete-permanently")
	public String deletePermanent(@PathVariable Long id) {

	    adminService.deletePermanent(id);

	    return "redirect:/admin#shops";
	}
	@GetMapping("/admin")
	public String dashboard(Model model, Principal principal) {

	    AccountEntity loginAdmin =
	            accountRepository.findByUserName(principal.getName())
	                    .orElseThrow();

	    model.addAttribute("loginAdmin", loginAdmin);

	    model.addAttribute("userList", accountRepository.findAll());

	    model.addAttribute("shopList", adminService.findAllActive());

	    model.addAttribute("trashedShopList", adminService.findTrash());

	    model.addAttribute("reviewList", reviewService.findAll());

	    model.addAttribute(
	            "approvalList",
	            deletionRequestService.getPendingApprovals());

	    model.addAttribute(
	            "editApprovalList",
	            shopEditService.findPending());

	    model.addAttribute(
	            "pendingEditApprovalCount",
	            shopEditService.countPending());

	    return "admin_dashboard";
	}
}
