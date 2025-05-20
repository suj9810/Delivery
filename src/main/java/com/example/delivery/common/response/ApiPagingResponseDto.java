package com.example.delivery.common.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.example.delivery.common.exception.enums.SuccessCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiPagingResponseDto<T> extends ResponseEntity<T> {
	private int statusCode;
	private String message;
	private Data<T> data;

	@Getter
	@Builder
	public static class Data<T> {
		private long totalElements;
		private int totalPages;
		private boolean hasNext;
		private boolean hasPrevious;
		private List<T> content;
	}

	public static <T> ApiPagingResponseDto<T> success(SuccessCode successCode, Page<T> page) {
		return ApiPagingResponseDto.<T>builder()
			.statusCode(successCode.getHttpStatus().value())
			.message(successCode.getMessage())
			.data(Data.<T>builder()
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.hasNext(page.hasNext())
				.hasPrevious(page.hasPrevious())
				.content(page.getContent())
				.build())
			.build();
	}

}
