
package com.Tabetomo.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LoginConfig {

	@Autowired
	private LoginService customUserDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// ★修正ポイント1: JavaScript(fetch)からのPOST通信で403を出さないため、APIのCSRFチェックを無効化
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/api/**")
				)
				.userDetailsService(customUserDetailsService)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								// お気に入り・いいね機能などのAPIはログイン必須
								"/api/shop/**",
								"/api/review/**"
						).authenticated()

						.requestMatchers(
								"/", 
								"/login", 
								"/register", 
								"/register/**",
								"/main/**", 
								"/css/**",
								"/js/**",
								"/images/**",
								"/Guest", "/Guest/**",
								"/shop/detail/**",
								"/review/form/**",
								"/shop/search",
								"/shop/search/**",
								"/mypage/**",
								"/api/shops/suggest",
								"/error", 
								"/favicon.ico"
						).permitAll()

						.requestMatchers("/admin/**","/admin").hasRole("ADMIN")

						.anyRequest().authenticated()
			)
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler((request, response, authentication) -> {

							boolean admin = authentication.getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							if (admin) {
								response.sendRedirect("/admin");
							} else {
								response.sendRedirect("/");
							}
						})
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
				);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}