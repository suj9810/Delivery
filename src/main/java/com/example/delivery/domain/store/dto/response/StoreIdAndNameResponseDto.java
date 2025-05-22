package com.example.delivery.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class StoreIdAndNameResponseDto {
    private final Long id;
    private final String storeName;
}
