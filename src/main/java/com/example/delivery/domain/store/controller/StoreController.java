package com.example.delivery.domain.store.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiPagingResponseDto;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.store.dto.request.SaveStoreRequestDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.delivery.domain.store.dto.response.SaveStoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoreIdAndNameResponseDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping()
    public ResponseEntity<ApiResponseDto<SaveStoreResponseDto>> saveStore (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SaveStoreRequestDto dto) {
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(userDetails, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.STORE_CREATED, saveStoreResponseDto));
    }

    // 전체 조회(가게명이 들어간 단어로 검색)
    @GetMapping()
    public ResponseEntity<ApiPagingResponseDto<StoreIdAndNameResponseDto>> getStores (
            @PageableDefault(direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @Validated @RequestParam String storeName
    ) {
        Page<StoreIdAndNameResponseDto> stores = storeService.getStores(pageable, storeName);

        return ResponseEntity.ok().body(ApiPagingResponseDto.success(SuccessCode.STORE_PAGING_SUCCESS, stores));
    }

    // 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<StoreResponseDto>> getStore (@PathVariable("storeId") Long storeId) {
        StoreResponseDto store = storeService.getStore(storeId);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_FIND_SUCCESS, store));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> updateStore (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("storeId") Long storeId,
            @RequestBody UpdateStoreRequestDto dto
    ) {
        storeService.updateStore(userDetails, storeId, dto);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_UPDATED));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteStore (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("storeId") Long storeId) {
        storeService.deleteStore(userDetails, storeId);
        return ResponseEntity.ok().body(ApiResponseDto.success(SuccessCode.STORE_CLOSED));
    }
}
