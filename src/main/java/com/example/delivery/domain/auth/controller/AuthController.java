package com.example.delivery.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.dto.LoginRequestDto;
import com.example.delivery.domain.auth.dto.LoginResponseDto;
import com.example.delivery.domain.auth.dto.UserDeleteRequestDto;
import com.example.delivery.domain.auth.dto.UserSignUpRequestDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.auth.service.AuthService;
import com.example.delivery.domain.user.dto.response.UserResponseDto;
import com.example.delivery.domain.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	/**
	 * 회원가입
	 * @param requestDto
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<ApiResponseDto<Object>> signup(
		@Valid @RequestBody UserSignUpRequestDto requestDto) {

		UserResponseDto response = authService.signup(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.SIGNUP_SUCCESS, response));
	}

	/**
	 * 로그인
	 * @param requestDto
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto> login(
		@RequestBody LoginRequestDto requestDto) {
		LoginResponseDto response = authService.login(requestDto);

		return ResponseEntity.ok(
			ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS, response)
		);
	}

	/**
	 * 로그아웃
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponseDto<?>> logout(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		User user = userDetails.getUser();
		authService.logout(user.getId());
		return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.LOGOUT_SUCCESS, null));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponseDto<Void>> deleteUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UserDeleteRequestDto requestDto
	) {
		authService.deleteUser(userDetails.getUser(), requestDto);

		return ResponseEntity.ok(
			ApiResponseDto.success(SuccessCode.WITHDRAWAL_SUCCESS, null)
		);
	}

}
