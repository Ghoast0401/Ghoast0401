package login;

public class LoginController {
	// ログイン画面を表示するだけ
	@GetMapping("login")
	public String login() {
		return "login";
}
	@PostMapping("logout")
	public String logout() {
		return "index";
	}

}
