package com.example.delivery.domain.reviews.service;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.request.ReviewUpdateRequest;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponseCache;
import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.reviews.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final StoreRepository storeRepository;

	private final RedisTemplate<String, Object> redisTemplateObject;

	private static final String CACHE_KEY = "reviews:all";
	private static final String PAGING_CACHE_KEY = "paging:reviews:all";

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

		// // 캐시에서 조회
		// Page<ReviewFindResponse> cachedReviews = (Page<ReviewFindResponse>) redisTemplate.opsForValue().get(PAGING_CACHE_KEY);
		// if (cachedReviews != null) {
		// 	return cachedReviews;
		// }

		// DB에서 조회
		Page<ReviewFindResponse> page = reviewRepository.findReviewWithCondition(condition, pageable);

		// // 캐시에 저장
		// redisTemplate.opsForValue().set(PAGING_CACHE_KEY, page);

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

	// 캐싱 없이
	public List<ReviewFindResponse> getReviewsWithoutCache() {
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
		return responses;
	}

	// 어노테이션을 활용한 caffeine(local-memory) 캐싱
	@Cacheable(value = "caffeineReviews", cacheManager = "caffeineCacheManager")
	public List<ReviewFindResponse> getReviewsWithCacheCaffeine() {

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

		return result;
	}

	// 수동으로 레디스에 캐싱하기
	public List<ReviewFindResponseCache> getReviewsWithCacheRedis() {
		// 캐시에서 조회
		Object cachedReviews = redisTemplateObject.opsForValue().get(CACHE_KEY);
		if (cachedReviews != null && cachedReviews instanceof List<?> list && !list.isEmpty()) {
			return (List<ReviewFindResponseCache>) list;
		}

		List<ReviewFindResponseCache> result = reviewRepository.findAll().stream()
			.map(review -> ReviewFindResponseCache.builder()
				.reviewId(review.getId())
				.storeId(review.getStore().getId())
				.rating(review.getRating())
				.content(review.getContent())
				.createdAt(review.getCreatedAt().toString())
				.modifiedAt(review.getUpdatedAt().toString())
				.build())
			.toList();

		redisTemplateObject.opsForValue().set(CACHE_KEY, result, Duration.ofMinutes(10));

		return result;
	}
}
