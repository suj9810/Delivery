package com.example.delivery.domain.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.menu.dto.request.MenuCreatRequest;
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

	@PostMapping
	public ResponseEntity<ApiResponseDto<MenuResponse>> createMenu(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody MenuCreatRequest request
	) {
		ApiResponseDto<MenuResponse> menu = menuService.createMenu(userDetails.getUser().getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(menu);
	}
}
