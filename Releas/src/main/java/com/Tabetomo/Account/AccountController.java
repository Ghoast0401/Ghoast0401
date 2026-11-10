package com.Tabetomo.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class AccountController {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// アカウント作成画面を表示
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("accountForm", new AccountForm());
		return "account_make";
	}

	// 「登録する」ボタン押下時（URLを /register に統一）
	@PostMapping("/register")
	public String signup(
			@Valid @ModelAttribute("accountForm") AccountForm form,
			BindingResult result,
			Model model) {

		// 1. 入力チェック（文字数や空欄エラーがあれば画面に戻す）
		if (result.hasErrors()) {
			return "account_make";
		}

		// 2. ユーザー名重複チェック
		if (accountRepository.findByUserName(form.getUserName()).isPresent()) {
			model.addAttribute("error", "そのユーザー名は既に使われています。");
			return "account_make";
		}

		// 3. 新しいアカウント作成と保存
		AccountEntity loginUser = new AccountEntity();
		loginUser.setUsername(form.getUserName());
		loginUser.setPassword(passwordEncoder.encode(form.getPassword()));
		loginUser.setRole("USER"); // デフォルト権限を設定

		accountRepository.save(loginUser);

        // 登録成功画面へリダイレクト
        return "account_register_success";
    }
	
}