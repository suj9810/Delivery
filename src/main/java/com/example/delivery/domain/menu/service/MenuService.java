package com.example.delivery.domain.menu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.menu.dto.request.MenuCreatRequest;
import com.example.delivery.domain.menu.dto.response.MenuResponse;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

/**
 * Menu service.
 */
@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public ApiResponseDto<MenuResponse> createMenu(Long loginUserId, MenuCreatRequest request) {
		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		validOwner(store.getUser().getId(), loginUserId);

		Menu menu = Menu.builder()
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.store(store)
			.build();
		menuRepository.save(menu);

		MenuResponse response = MenuResponse.builder()
			.id(menu.getId())
			.name(menu.getName())
			.description(menu.getDescription())
			.price(menu.getPrice())
			.build();

		return ApiResponseDto.success(SuccessCode.MENU_CREATED, response);
	}

	// private Menu findMenu(Long menuId, Long loginUserId) {
	// 	Menu menu = menuRepository.findById(menuId)
	// 		.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
	// 	validOwner(menu.getStore().getUser().getId(), loginUserId);
	// 	return menu;
	// }

	// private Menu findIdOrElseThrow(Long menuId) {
	// 	return menuRepository.findById(menuId)
	// 		.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
	// }

	private void validOwner(Long ownerId, Long loginUserId) {
		if (!ownerId.equals(loginUserId)) {
			throw new CustomException(ErrorCode.OWNER_PERMISSION_REQUIRED);
		}
	}
}
