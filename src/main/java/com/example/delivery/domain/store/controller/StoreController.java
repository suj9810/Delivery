package com.example.delivery.domain.store.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.store.dto.request.SaveStoreRequestDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.delivery.domain.store.dto.response.SaveStoreResponseDto;
import com.example.delivery.domain.store.dto.response.StorePageResponse;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping()
    public ResponseEntity<ApiResponseDto<SaveStoreResponseDto>> saveStore (
            // userId 들어가야 함
            @RequestBody SaveStoreRequestDto dto) {
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.STORE_CREATED, saveStoreResponseDto));
    }

    // 전체 조회(가게명이 들어간 단어로 검색)
    @GetMapping()
    public ResponseEntity<ApiResponseDto<StorePageResponse>> getStores (
            @PageableDefault(direction = Sort.Direction.ASC, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String storeName
    ) {
        StorePageResponse stores = storeService.getStores(pageable, storeName);

        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_PAGING_SUCCESS, stores));
    }

    // 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<StoreResponseDto>> getStore (@PathVariable Long storeId) {
        StoreResponseDto store = storeService.getStore(storeId);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.OK, store));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> updateStore (
            @PathVariable Long storeId,
            @RequestBody UpdateStoreRequestDto dto
    ) {
        storeService.updateStore(storeId, dto);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_UPDATED));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteStore (@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_CLOSED));
    }
}
