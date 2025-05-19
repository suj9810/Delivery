package com.example.delivery.domain.reviews.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewPageResponse;
import com.example.delivery.domain.reviews.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<ApiResponseDto<Long>> createReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody ReviewCreateRequest dto
	) {
		ApiResponseDto<Long> apiResponseDto = reviewService.saveReview(userDetails, dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDto);
	}

	@GetMapping
	public ResponseEntity<ApiResponseDto<ReviewPageResponse>> findReview(
		@ModelAttribute ReviewFindCondition condition,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		ReviewPageResponse reviews = reviewService.getReviews(condition, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.OK, reviews));
	}

	@PutMapping("/{reviewId}")
	public ResponseEntity<ApiResponseDto<Long>> updateReview(
		@PathVariable("reviewId") Long reviewId,
		@RequestBody ReviewUpdateRequest dto
	) {
		ApiResponseDto<Long> apiResponseDto = reviewService.updateReview(reviewId, dto);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDto);
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<ApiResponseDto<Long>> delete(
		@PathVariable("reviewId") Long reviewId
	) {
		ApiResponseDto<Long> apiResponseDto = reviewService.deleteReview(reviewId);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDto);
	}

}
