package com.example.delivery.domain.reviews.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.dto.response.ReviewPageResponse;
import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.reviews.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final com.example.delivery.domain.user.repository.UserRepository userRepository;
	private final StoreRepository storeRepository;

	public ApiResponseDto<Long> saveReview(Long userId, ReviewCreateRequest dto) {

		// 시큐리티 사용할것
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException());

		Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new RuntimeException());

		Review review = Review.builder()
			.user(user)
			.rating(dto.getRating())
			.content(dto.getContent())
			.store(store)
			.build();

		reviewRepository.save(review);
		return ApiResponseDto.success(SuccessCode.OK, review.getId());
	}

	public ReviewPageResponse getReviews(ReviewFindCondition condition, Pageable pageable) {
		Page<Review> page = reviewRepository.findReviewWithCondition(condition, pageable);

        List<ReviewFindResponse> content = page.getContent().stream()
            .map(ReviewFindResponse::from)
            .toList();

        return ReviewPageResponse.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNextPage(page.hasNext())
            .hasPreviousPage(page.hasPrevious())
            .content(content)
            .build();
	}

	@Transactional
	public ApiResponseDto<Long> updateReview(Long reviewId, ReviewUpdateRequest dto) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException());
		review.update(dto.getContent());
		return ApiResponseDto.success(SuccessCode.OK, review.getId());
	}

	public ApiResponseDto<Long> deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException());
		reviewRepository.delete(review);
		return ApiResponseDto.success(SuccessCode.REVIEW_DELETED);
	}
}
