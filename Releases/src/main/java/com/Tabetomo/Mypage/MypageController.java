package com.Tabetomo.Mypage;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;
import com.Tabetomo.Review.ReviewEntity;
import com.Tabetomo.Review.ReviewForm;

@Controller
public class MypageController {

	@Autowired
	private MypageService mypageService;

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping("/mypage")
	public String mypage(Principal principal, Model model) {

	    if (principal == null) {
	        return "redirect:/login";
	    }

	    AccountEntity account = accountRepository
	            .findByUserName(principal.getName())
	            .orElse(null);

	    if (account == null) {
	        return "redirect:/login";
	    }

	    List<ReviewEntity> reviewList =
	            mypageService.getMyReview(account.getId());

	    model.addAttribute("user", account);
	    model.addAttribute("myReviewList", reviewList);

	    return "mypage";
	}

	@GetMapping("/mypage/review/edit/{id}")
	public String edit(
	        @PathVariable Long id,
	        Principal principal,
	        Model model) {

	    AccountEntity account =
	            accountRepository.findByUserName(principal.getName())
	            .orElseThrow();

	    ReviewEntity review =
	            mypageService.getReviewById(id);

	    if(!review.getUserId().equals(account.getId())) {
	        return "redirect:/mypage";
	    }

	    ReviewForm form = new ReviewForm();

	    form.setReviewId(review.getId());
	    form.setShopId(review.getShop().getId());
	    form.setRating(review.getRating());
	    form.setComment(review.getComment());

	    model.addAttribute("reviewForm", form);

	    return "shop_edit";
	}

	@PostMapping("/mypage/review/update")
	public String update(ReviewForm form) {

	    mypageService.updateReview(
	            form.getReviewId(),
	            form.getRating(),
	            form.getComment());

	    return "redirect:/mypage";
	}

	@PostMapping("/mypage/review/delete/{id}")
	public String delete(
	        @PathVariable Long id,
	        Principal principal) {

	    AccountEntity account =
	            accountRepository.findByUserName(principal.getName())
	            .orElseThrow();

	    mypageService.deleteReview(id, account.getId());

	    return "redirect:/mypage";
	}
}