package com.example.delivery.domain.reviews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class ReviewFindCondition {
	private Long storeId;
	private Long minRating;
	private Long maxRating;
}
