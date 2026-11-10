package account_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.Tabetomo.Account.AccountController;

class AccountControllerTest {
	
	@Autowired
    private MockMvc mockMvc; // 疑似的なHTTPリクエストを送るためのクラス

	@Test
	@DisplayName("会員登録画面（/register）にアクセスした際、正しいビューと初期フォームが返されること")
	void test() {
		AccountController accountController = new AccountController();
		fail("まだ実装されていません");
	}

}
