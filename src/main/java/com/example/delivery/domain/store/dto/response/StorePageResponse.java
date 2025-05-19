package com.example.delivery.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StorePageResponse {
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private final List<StoreIdAndNameResponseDto> content;
}
