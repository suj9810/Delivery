package com.example.delivery.common.config;

import com.example.delivery.common.exception.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.delivery.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.delivery.domain.auth.jwt.TokenProvider;
import com.example.delivery.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	private static final String[] AUTH_WHITELIST = {
		"/auth/signup", "/auth/login", "/users/{id}",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf, cors 설정
		http.csrf((csrf) -> csrf.disable());
		http.cors(Customizer.withDefaults());

		// 세션 관리 상태 없음
		http.sessionManagement(
			sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// formLogin, httpBasic 비활성화
		http.formLogin((form) -> form.disable());
		http.httpBasic(AbstractHttpConfigurer::disable);

		// Filter 적용
		http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider, userRepository),
			UsernamePasswordAuthenticationFilter.class);

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/stores").hasRole("OWNER")
				.requestMatchers(HttpMethod.PATCH, "/stores/*").hasRole("OWNER")
				.requestMatchers(HttpMethod.DELETE, "/stores/*").hasRole("OWNER")
				.requestMatchers(HttpMethod.GET, "/stores/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/menus").hasRole("OWNER")
				.requestMatchers(HttpMethod.PUT, "menus/*").hasRole("OWNER")
				.requestMatchers(HttpMethod.DELETE, "/menus/*").hasRole("OWNER")
				.requestMatchers(AUTH_WHITELIST).permitAll() // 비회원도 접근 가능한 경로, 인증 없이 접근 허용
				.anyRequest().authenticated() // 그외 경로 : 반드시 인증 진행
		);

		http.exceptionHandling(exceptions -> exceptions
				.accessDeniedHandler(customAccessDeniedHandler));

		return http.build();
	}
}
