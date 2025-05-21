package com.example.delivery.domain.menu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.menu.dto.request.MenuCreatRequest;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequest;
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

	/**
	 * 메뉴 생성
	 *
	 * @param loginUserId the login user id 
	 * @param request the request 
	 * @return the api response dto
	 */
	@Transactional
	public MenuResponse createMenu(Long loginUserId, MenuCreatRequest request) {
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

		return MenuResponse.of(menu);
	}

	@Transactional(readOnly = true)
	public Page<MenuResponse> getMenusByStore(Long storeId, Pageable pageable) {
		return menuRepository.findMenusByStoreId(storeId, pageable);
	}

	/**
	 * 메뉴 수정
	 *
	 * @param loginUserid the login userid 
	 * @param menuId the menu id 
	 * @param request the request 
	 * @return the api response dto
	 */
	@Transactional
	public MenuResponse updateMenu(Long loginUserid, Long menuId, MenuUpdateRequest request) {
		Menu menu = findMenuWithStoreAndUser(menuId);
		validOwner(menu.getStore().getUser().getId(), loginUserid);
		menu.updateMenu(request);
		return MenuResponse.of(menu);
	}

	/**
	 * 메뉴 삭제
	 *
	 * @param loginUserid the login userid 
	 * @param menuId the menu id 
	 * @return the api response dto
	 */
	@Transactional
	public void deleteMenu(Long loginUserid, Long menuId) {
		Menu menu = findMenuWithStoreAndUser(menuId);
		validOwner(menu.getStore().getUser().getId(), loginUserid);
		menuRepository.delete(menu);
	}

	/**
	 * 메뉴 ID 검증
	 * @param menuId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Menu findMenuWithStoreAndUser(Long menuId) {
		return menuRepository.findMenuById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
	}

	/**
	 * 오너 ID, 로그인 ID 검증
	 * @param ownerId
	 * @param loginUserId
	 */
	private void validOwner(Long ownerId, Long loginUserId) {
		if (!ownerId.equals(loginUserId)) {
			throw new CustomException(ErrorCode.OWNER_PERMISSION_REQUIRED);
		}
	}
}
