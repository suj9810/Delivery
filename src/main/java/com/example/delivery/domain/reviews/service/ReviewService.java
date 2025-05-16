package com.example.delivery.domain.reviews.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.delivery.domain.User.entity.User;
import com.example.delivery.domain.User.repository.UserRepository;
import com.example.delivery.domain.reviews.dto.request.ReviewCreateRequest;
import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.dto.response.ReviewPageResponse;
import com.example.delivery.domain.reviews.entity.Review;
import com.example.delivery.domain.reviews.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public void saveReview(ReviewCreateRequest dto) {

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

}
