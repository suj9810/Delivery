package com.example.delivery.domain.favorite.repository;

import com.example.delivery.domain.favorite.dto.FavoriteStoreDto;

import java.util.List;

public interface FavoriteQueryRepository {
    // 즐겨찾기 된 가게 dto 리스트 조회
    // 필요한 필드만 조회되도록 함
    List<FavoriteStoreDto> findFavoriteStoreByUserId(Long userId);
}