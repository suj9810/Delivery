package com.example.delivery.domain.menu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.delivery.domain.menu.dto.response.MenuResponse;

public interface MenuRepositoryCustom {
	Page<MenuResponse> findMenusByStoreId(Long storeId, Pageable pageable);
}
