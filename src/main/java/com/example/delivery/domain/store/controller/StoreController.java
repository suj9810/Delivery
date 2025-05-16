package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.store.dto.request.SaveStoreRequestDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.delivery.domain.store.dto.response.SaveStoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping()
    public ResponseEntity<SaveStoreResponseDto> saveStore (
            @ userId
            @RequestBody SaveStoreRequestDto dto) {
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(dto);
        return ResponseEntity.ok(saveStoreResponseDto);
    }

    // 전체 조회(가게명이 들어간 단어로 검색)
    @GetMapping()
    public ResponseEntity<Page<StoreNameResponseDto>> getStores (
            @PageableDefault() Pageable pageable,
            @RequestParam(required = false) String storeName
    ) {
        storeService.getStores(pageable, storeName);
    }

    // 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore (@PathVariable Long storeId) {
        StoreResponseDto store = storeService.getStore(storeId);
        return ResponseEntity.ok(store);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> updateStore (
            @PathVariable Long storeId,
            @RequestBody UpdateStoreRequestDto dto
    ) {
        storeService.updateStore(storeId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore (@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok().build();
    }
}
