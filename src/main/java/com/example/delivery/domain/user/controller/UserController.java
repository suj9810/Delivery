package com.example.delivery.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.delivery.domain.user.dto.request.UserUpdateRequestDto;
import com.example.delivery.domain.user.dto.response.UserResponseDto;
import com.example.delivery.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	/**
	 * 회원 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(
		@PathVariable Long id) {
		UserResponseDto data = userService.getUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.USER_FETCH_SUCCESS, data));
	}

	/**
	 * 정보 수정
	 * @param requestDto
	 * @param userDetails
	 * @return
	 */
	@PutMapping("/me")
	public ResponseEntity<ApiResponseDto<UserUpdateRequestDto>> updateUserInfo(
		@Valid @RequestBody UserUpdateRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UserUpdateRequestDto updatedUser = userService.updateUserInfo(userDetails.getUser(), requestDto);

		return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.USER_UPDATE_SUCCESS, updatedUser)
		);
	}

	/**
	 * 비밀번호 업데이트
	 * @param requestDto
	 * @param userDetails
	 * @return
	 */
	@PatchMapping("/{id}/update-password")
	public ResponseEntity<ApiResponseDto<Void>> updatePassword(
		@Valid @RequestBody UserPasswordUpdateRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		userService.updatePassword(userDetails.getUser(), requestDto);
		return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.PASSWORD_UPDATE_SUCCESS, null)
		);
	}

}
