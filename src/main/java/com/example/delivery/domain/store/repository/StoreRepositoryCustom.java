package com.example.delivery.domain.store.repository;


import com.example.delivery.domain.store.dto.response.StoreIdAndNameResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {
    Page<StoreIdAndNameResponseDto> findStoreIdAndStoreNameByStoreName(Pageable pageable, String storeName);
}
