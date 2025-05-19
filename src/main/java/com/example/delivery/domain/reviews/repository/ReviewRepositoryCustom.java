package com.example.delivery.domain.reviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.entity.Review;

public interface ReviewRepositoryCustom {
	Page<Review> findReviewWithCondition(ReviewFindCondition condition, Pageable pageable);
}
