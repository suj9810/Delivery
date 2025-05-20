package com.example.delivery.domain.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.menu.dto.request.MenuCreatRequest;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequest;
import com.example.delivery.domain.menu.dto.response.MenuResponse;
import com.example.delivery.domain.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

/**
 * Menu controller.
 */
@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	/**
	 * 메뉴 생성
	 *
	 * @param userDetails the user details
	 * @param request the request
	 * @return the response entity
	 */
	@PostMapping
	public ResponseEntity<ApiResponseDto<MenuResponse>> createMenu (
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody MenuCreatRequest request
	) {
		MenuResponse response = menuService.createMenu(userDetails.getUser().getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.MENU_CREATED, response));
	}

	/**
	 * 메뉴 수정
	 *
	 * @param userDetails the user details
	 * @param menuId the menu id
	 * @param request the request
	 * @return the response entity
	 */
	@PutMapping("/{menuId}")
	public ResponseEntity<ApiResponseDto<MenuResponse>> updateMenu (
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long menuId,
		@RequestBody MenuUpdateRequest request
	) {
		MenuResponse response = menuService.updateMenu(userDetails.getUser().getId(), menuId, request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.MENU_UPDATED, response));
	}

	/**
	 * 메뉴 삭제
	 *
	 * @param userDetails the user details 
	 * @param menuId the menu id 
	 * @return the response entity
	 */
	@DeleteMapping("/{menuId}")
	public ResponseEntity<ApiResponseDto<Void>> deleteMenu (
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable Long menuId
	) {
		menuService.deleteMenu(userDetails.getUser().getId(), menuId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.success(SuccessCode.MENU_DELETED));
	}
}
