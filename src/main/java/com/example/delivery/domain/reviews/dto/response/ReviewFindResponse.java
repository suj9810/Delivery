package com.example.delivery.domain.reviews.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewFindResponse {
	private long reviewId;
	private long storeId;
	private int rating;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	@QueryProjection
	public ReviewFindResponse(long reviewId, long storeId, int rating, String content, LocalDateTime createdAt,
		LocalDateTime modifiedAt) {
		this.reviewId = reviewId;
		this.storeId = storeId;
		this.rating = rating;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}
