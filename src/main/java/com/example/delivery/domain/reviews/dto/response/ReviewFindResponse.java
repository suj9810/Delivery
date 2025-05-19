package com.example.delivery.domain.reviews.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewFindResponse {
	private long reviewId;
	private long storeId;
	private int rating;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static ReviewFindResponse from(Review review) {
		return ReviewFindResponse.builder()
			.reviewId(review.getId())
			.storeId(review.getStore().getId())
			.rating(review.getRating())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.modifiedAt(review.getUpdatedAt())
			.build();
	}
}
