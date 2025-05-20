package com.example.delivery.domain.reviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiPagingResponseDto;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.reviews.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	public Long saveReview(UserDetailsImpl userDetails, ReviewCreateRequest dto) {
		Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		Review review = Review.builder()
			.user(userDetails.getUser())
			.rating(dto.getRating())
			.content(dto.getContent())
			.store(store)
			.build();

		reviewRepository.save(review);
		return review.getId();
	}

	public Page<ReviewFindResponse> getReviews(ReviewFindCondition condition, Pageable pageable) {
		Page<ReviewFindResponse> page = reviewRepository.findReviewWithCondition(condition, pageable);

		return page;
	}

	@Transactional
	public Long updateReview(UserDetailsImpl user, Long reviewId, ReviewUpdateRequest dto) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		review.validateOwner(user.getUser());

		review.update(dto.getContent());
		return review.getId();
	}

	@Transactional
	public void deleteReview(UserDetailsImpl user, Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		review.validateOwner(user.getUser());

		reviewRepository.delete(review);
	}
}
