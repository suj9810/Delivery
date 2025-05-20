package com.example.delivery.domain.reviews.controller;

import org.springframework.data.domain.Page;
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
import com.example.delivery.common.response.ApiPagingResponseDto;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 생성
	 * @param userDetails 로그인한 유저 정보
	 * @param dto 리뷰 생성 정보(content, rating, storeId)
	 * @return 리뷰 생성 성공 여부 및 생성된 리뷰 ID
	 */
	@PostMapping
	public ResponseEntity<ApiResponseDto<Long>> createReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody ReviewCreateRequest dto
	) {
		Long apiResponseDto = reviewService.saveReview(userDetails, dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.REVIEW_CREATED, apiResponseDto));
	}

	/**
	 * 리뷰 조회
	 * @param condition 리뷰 조회 조건
	 * @param pageable 페이징 정보
	 * @return 조회된 리뷰
	 */
	@GetMapping
	public ResponseEntity<ApiPagingResponseDto<ReviewFindResponse>> findReview(
		@ModelAttribute ReviewFindCondition condition,
		@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<ReviewFindResponse> reviews = reviewService.getReviews(condition, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(ApiPagingResponseDto.success(SuccessCode.REVIEW_SUCCESS_FIND, reviews));
	}

	/**
	 * 리뷰 수정
	 * @param userDetails 로그인한 유저 정보
	 * @param reviewId 리뷰ID
	 * @param dto 수정할 리뷰 정보(content)
	 * @return 수정된 리뷰
	 */
	@PutMapping("/{reviewId}")
	public ResponseEntity<ApiResponseDto<Long>> updateReview(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable("reviewId") Long reviewId,
		@RequestBody ReviewUpdateRequest dto
	) {
		Long apiResponseDto = reviewService.updateReview(userDetails, reviewId, dto);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.REVIEW_UPDATED, apiResponseDto));
	}

	/**
	 * 리뷰 삭제
	 * @param userDetails 로그인한 유저 정보
	 * @param reviewId 삭헤라 리뷰 ID
	 * @return 삭제 성공 여부
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<ApiResponseDto<Long>> delete(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PathVariable("reviewId") Long reviewId
	) {
		reviewService.deleteReview(userDetails, reviewId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.REVIEW_DELETED));
	}

}
