package com.Tabetomo.Review;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Tabetomo.Detail.DetailDto;
import com.Tabetomo.Detail.DetailService;
import com.Tabetomo.shop.PhotoService;
import com.Tabetomo.shop.ShopRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private DetailService detailService;
	@Autowired
	private PhotoService photoService;

	//投稿フォーム画面表示
	// ① 投稿フォーム画面表示
	@GetMapping("/form/{id}")
	public String review(@PathVariable("id") Long id, Model model) {

		DetailDto detailDto = detailService.getDetail(id, null);

		ReviewForm reviewForm = new ReviewForm();
		reviewForm.setShopId(id);

		model.addAttribute("detailDto", detailDto);
		model.addAttribute("reviewForm", new ReviewForm());
		return "review_form";
	}

	@PostMapping("/confirm")
	public String confirm(@Valid @ModelAttribute ReviewForm reviewForm,
			BindingResult result,
			HttpSession session,
			Model model) {

		if (result.hasErrors()) {
			DetailDto detailDto = detailService.getDetail(reviewForm.getShopId(), null);
			model.addAttribute("detailDto", detailDto);
			model.addAttribute("reviewForm", reviewForm);
			return "review_form";
		}

		DetailDto detailDto = detailService.getDetail(reviewForm.getShopId(), null);

		model.addAttribute("detailDto", detailDto);
		model.addAttribute("reviewForm", reviewForm);

		session.setAttribute("reviewForm", reviewForm);

		// アップロードされた写真は、確認画面をまたぐ間だけバイト配列にしてセッションに一時保存しておく
		// （実際のファイル保存・DB登録は投稿確定時のsubmit()で行う）
		List<PendingPhoto> pendingPhotos = PhotoService.toPendingPhotos(reviewForm.getPhotos());
		session.setAttribute("reviewPhotos", pendingPhotos);
		model.addAttribute("photoCount", pendingPhotos.size());

		return "review_confirm";
	}

	// ②→① [戻る] ボタン押下時(入力内容を保持したままフォームへ)
	@PostMapping("/back")
	public String back(HttpSession session, Model model) {

		ReviewForm form = (ReviewForm) session.getAttribute("reviewForm");

		DetailDto detailDto = detailService.getDetail(form.getShopId(), null);

		model.addAttribute("detailDto", detailDto);
		model.addAttribute("reviewForm", form);

		return "review_form";
	}

	@PostMapping("/submit")
	public String submit(@Valid @ModelAttribute ReviewForm reviewForm,
			BindingResult result,
			Model model,
			HttpSession session,
			Principal principal) { // ★追加
		System.out.println("★★★★★ submit() が呼ばれました ★★★★★");
		if (result.hasErrors()) {
			result.getFieldErrors()
					.forEach(error -> System.out.println(error.getField() + " : " + error.getDefaultMessage()));
			DetailDto detailDto = detailService.getDetail(reviewForm.getShopId(), null);
			model.addAttribute("detailDto", detailDto);
			model.addAttribute("reviewForm", reviewForm);
			return "review_form";
		}
		System.out.println("保存開始");
		ReviewEntity review =
		        reviewService.save(reviewForm, principal.getName());

		// confirm()でセッションに一時保存しておいた写真を、ここで実際に保存する
		@SuppressWarnings("unchecked")
		List<PendingPhoto> pendingPhotos = (List<PendingPhoto>) session.getAttribute("reviewPhotos");
		photoService.savePending(pendingPhotos, review.getId());
		session.removeAttribute("reviewPhotos");
		session.removeAttribute("reviewForm");

		System.out.println("保存終了");
		model.addAttribute("message", "店舗の登録が完了しました。");
		return "review_submit_success";

	}
}