package com.example.delivery.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuUpdateRequest {

	private Long storeId;
	private String name;
	private String description;
	private Integer price;
}
