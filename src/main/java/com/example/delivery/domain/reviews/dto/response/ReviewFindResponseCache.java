package com.example.delivery.domain.reviews.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewFindResponseCache {
	private long reviewId;
	private long storeId;
	private int rating;
	private String content;
	private String createdAt;
	private String modifiedAt;

	@QueryProjection
	public ReviewFindResponseCache(long reviewId, long storeId, int rating, String content, String createdAt,
		String modifiedAt) {
		this.reviewId = reviewId;
		this.storeId = storeId;
		this.rating = rating;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}
