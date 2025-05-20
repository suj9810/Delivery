package com.example.delivery.domain.menu.dto.response;

import com.example.delivery.domain.menu.entity.Menu;

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

	public static MenuResponse of(Menu menu) {
		return MenuResponse.builder()
				.id(menu.getId())
				.name(menu.getName())
				.description(menu.getDescription())
				.price(menu.getPrice())
				.build();
	}
}
