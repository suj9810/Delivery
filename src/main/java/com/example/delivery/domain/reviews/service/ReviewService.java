package com.example.delivery.domain.reviews.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.reviews.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final com.example.delivery.domain.user.repository.UserRepository userRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public Long saveReview(UserDetailsImpl userDetails, ReviewCreateRequest dto) {

		Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new CustomException(
			ErrorCode.STORE_NOT_FOUND));

		Review review = Review.builder()
			.user(userDetails.getUser())
			.rating(dto.getRating())
			.content(dto.getContent())
			.store(store)
			.build();

		reviewRepository.save(review);
		return review.getId();
	}

	@Transactional(readOnly = true)
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

	public List<ReviewFindResponse> getReviewsWithoutCache() {
		long start = System.currentTimeMillis();

		List<ReviewFindResponse> responses = reviewRepository.findAll().stream()
			.map(review -> ReviewFindResponse.builder()
				.reviewId(review.getId())
				.storeId(review.getStore().getId())
				.rating(review.getRating())
				.content(review.getContent())
				.createdAt(review.getCreatedAt())
				.modifiedAt(review.getUpdatedAt())
				.build())
			.toList();

		long end = System.currentTimeMillis();
		System.out.println("[Without Cache] 소요 시간: " + (end - start) + "ms");

		return responses;
	}

	@Cacheable(value = "caffeineReviews", cacheManager = "caffeineCacheManager")
	public List<ReviewFindResponse> getReviewsWithCache() {
		long start = System.currentTimeMillis();

		List<ReviewFindResponse> result = reviewRepository.findAll().stream()
			.map(review -> ReviewFindResponse.builder()
				.reviewId(review.getId())
				.storeId(review.getStore().getId())
				.rating(review.getRating())
				.content(review.getContent())
				.createdAt(review.getCreatedAt())
				.modifiedAt(review.getUpdatedAt())
				.build())
			.toList();

		long end = System.currentTimeMillis();
		System.out.println("[With Cache] 소요 시간: " + (end - start) + "ms");

		return result;
	}
}
