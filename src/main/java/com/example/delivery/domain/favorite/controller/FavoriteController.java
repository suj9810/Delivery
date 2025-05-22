package com.example.delivery.domain.favorite.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.favorite.dto.FavoriteStoreDto;
import com.example.delivery.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 즐겨찾기 추가
    @PostMapping("/{userId}/favorites/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> addFavorite(@PathVariable Long userId, @PathVariable Long storeId) {
        favoriteService.validateUser(userId);
        favoriteService.addFavorite(userId, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.CREATE));

    }
    // 즐겨찾기 삭제
    @DeleteMapping("/{userId}/favorites/{storeId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteFavorite(@PathVariable Long userId, @PathVariable Long storeId) {
        favoriteService.validateUser(userId);
        favoriteService.deleteFavorite(userId, storeId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.FAVORITE_DELETED));
    }

    //특정 사용자의 즐겨찾기 목록 조회 (본인 요청만 허용)
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<ApiResponseDto<List<FavoriteStoreDto>>> getFavoriteList(@PathVariable Long userId) {
        favoriteService.validateUser(userId);
        List<FavoriteStoreDto> result = favoriteService.getMyFavorites(userId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.FAVORITE_LIST, result));
    }
}