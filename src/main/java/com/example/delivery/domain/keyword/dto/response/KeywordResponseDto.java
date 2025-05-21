package com.example.delivery.domain.keyword.dto.response;

import lombok.Getter;

@Getter
public class KeywordResponseDto {

    private String storeName;

    public KeywordResponseDto(String storeName) {
        this.storeName = storeName;
    }
}
