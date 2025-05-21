package com.example.delivery.domain.menu.dto.response;

import com.example.delivery.domain.menu.entity.Menu;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Menu response.
 */
@Getter
public class MenuResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final Integer price;

	@Builder
	public MenuResponse(Long id, String name, String description, Integer price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	@QueryProjection
	public MenuResponse(Long id, String name, Integer price, String description) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public static MenuResponse of(Menu menu) {
		return MenuResponse.builder()
				.id(menu.getId())
				.name(menu.getName())
				.description(menu.getDescription())
				.price(menu.getPrice())
				.build();
	}
}
