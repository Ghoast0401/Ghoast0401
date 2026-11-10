package com.Tabetomo.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Tabetomo.Account.AccountEntity;
import com.Tabetomo.Account.AccountRepository;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AccountEntity account = accountRepository
                .findByUserName(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("ユーザーが見つかりません"));

        return User.builder()
				.username(account.getUsername())
				.password(account.getPassword())
				.roles(account.getRole())
				.build();
    }
}