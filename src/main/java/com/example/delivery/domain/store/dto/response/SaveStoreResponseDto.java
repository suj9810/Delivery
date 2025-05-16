package com.example.delivery.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class SaveStoreResponseDto {
    private final Long id;
    private final String storeName;
    private final String address;
    private final String phoneNumber;
    private final Long minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closedTime;
}
