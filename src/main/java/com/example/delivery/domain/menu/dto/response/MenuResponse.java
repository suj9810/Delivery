package com.example.delivery.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Menu response.
 */
@Getter
@Builder
@AllArgsConstructor
public class MenuResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final Integer price;
}
