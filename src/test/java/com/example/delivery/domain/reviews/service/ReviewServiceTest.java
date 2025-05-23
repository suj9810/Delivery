package com.example.delivery.domain.reviews.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.repository.ReviewRepository;

@SpringBootTest
class ReviewServiceTest {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplateObject;

	private final String PAGING_CACHE_KEY = "review:first-page";

	@Test
	@DisplayName("getReviewsCache")
	void getReviewTest () {
	    // given
		ReviewFindCondition reviewFindCondition = new ReviewFindCondition();
		ReflectionTestUtils.setField(reviewFindCondition, "storeId", 1L);

		System.out.println("reviewFindCondition = " + reviewFindCondition);

		Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

		redisTemplateObject.delete(PAGING_CACHE_KEY);

	}

}
