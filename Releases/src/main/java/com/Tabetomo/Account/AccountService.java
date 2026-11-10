package com.Tabetomo.Account;

	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

	@Service
	public class AccountService {

	    @Autowired
	    private AccountRepository accountRepository;
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    // 新規登録を実行するメソッド
	    public AccountEntity register(Long userId, String userName, String password) {
	        // 1. Entityのインスタンス（箱）を作成
	        AccountEntity account = new AccountEntity();

	        // 2. 引数で受け取ったデータをすべてセットする（IDも必須！）
	        account.setUserId(userId);
	        account.setUsername(userName);
	        account.setPassword(passwordEncoder.encode(password));

	        // 3. Postgresに保存（INSERT文が自動発行される）
	        return accountRepository.save(account);
	    }
	    public AccountEntity login(String userName, String password) {
	        // DBを検索して、存在すればtrue、いなければfalseを返す
	        return accountRepository.findByUserNameAndPassword(userName,password).orElse(null);
	    }

	}