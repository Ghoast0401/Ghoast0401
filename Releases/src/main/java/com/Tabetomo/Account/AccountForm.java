package com.Tabetomo.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountForm {

	@NotBlank(message = "ユーザー名を入力してください")
	@Size(min = 2, max = 20, message = "ユーザー名は2文字以上20文字以内で入力してください")
	private String userName;

	@NotBlank(message = "パスワードを入力してください")
	@Size(min = 8, max = 100, message = "パスワードは8文字以上で入力してください")
	private String password;

	// Getter & Setter
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}