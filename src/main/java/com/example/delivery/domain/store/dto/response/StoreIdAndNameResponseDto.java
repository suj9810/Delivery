package com.example.delivery.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreIdAndNameResponseDto {
    private final Long id;
    private final String storeName;
}
