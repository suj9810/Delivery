package com.example.delivery.domain.keyword.dto.response;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;

@Getter
public class KeywordResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String storeName;

    public KeywordResponseDto() {
        // Jackson이 기본 생성자를 통해 객체를 만들 수 있도록 함
    }

    public KeywordResponseDto(String storeName) {
        this.storeName = storeName;
    }
}
