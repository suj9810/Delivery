package com.example.delivery.domain.favorite.dto;

import com.example.delivery.domain.store.entity.Store;

import java.time.LocalTime;

public record FavoriteStoreDto(
        Long storeId,
        String storeName,
        String address,
        Long minOrderPrice,
        LocalTime openTime,
        LocalTime closedTime
){
    public FavoriteStoreDto(Store store) {
        this(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getMinOrderPrice(),
                store.getOpenTime(),
                store.getClosedTime());

    }
}