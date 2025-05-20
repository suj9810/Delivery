package com.example.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuCreatRequest {

	@NotNull(message = "스토어 ID는 필수입니다.")
	private Long storeId;

	@NotBlank(message = "메뉴 이름은 필수입니다.")
	private String name;

	@Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
	private String description;

	@NotNull(message = "가격은 필수입니다.")
	@Min(value = 0, message = "가격은 0 이상 이어야 합니다.")
	private Integer price;
}
