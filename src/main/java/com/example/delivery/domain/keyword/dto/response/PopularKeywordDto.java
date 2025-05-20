package com.example.delivery.domain.keyword.dto.response;

import lombok.Getter;

@Getter
public class PopularKeywordDto {

    private Integer rank;
    private String storeName;

    public PopularKeywordDto(Integer rank, String storeName) {
        this.rank = rank;
        this.storeName = storeName;
    }

}
