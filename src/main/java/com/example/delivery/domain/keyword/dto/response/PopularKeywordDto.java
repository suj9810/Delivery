package com.example.delivery.domain.keyword.dto.response;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;

@Getter
public class PopularKeywordDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer rank;
    private String keyword;

    public PopularKeywordDto() {

    }

    public PopularKeywordDto(Integer rank, String keyword) {
        this.rank = rank;
        this.keyword = keyword;
    }

}
