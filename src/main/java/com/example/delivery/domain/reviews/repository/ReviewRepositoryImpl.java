package com.example.delivery.domain.reviews.repository;

import static com.example.delivery.domain.reviews.entity.QReview.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.example.delivery.domain.reviews.dto.request.ReviewFindCondition;
import com.example.delivery.domain.reviews.dto.response.QReviewFindResponse;
import com.example.delivery.domain.reviews.dto.response.ReviewFindResponse;
import com.example.delivery.domain.reviews.entity.QReview;
import com.example.delivery.domain.reviews.entity.Review;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	public ReviewRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<ReviewFindResponse> findReviewWithCondition(ReviewFindCondition condition, Pageable pageable) {
		List<ReviewFindResponse> content = queryFactory
			.select(new QReviewFindResponse(
				review.id,
				review.store.id,
				review.rating,
				review.content,
				review.createdAt,
				review.updatedAt
			))
			.from(review)
			.where(
				// review.store.id.eq(1L),
				// minRating(condition.getMinRating()),
				// maxRating(condition.getMaxRating())
				review.store.id.eq(1L)
			)
			.orderBy(review.createdAt.desc(), review.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Review> countQuery = queryFactory
			.select(review)
			.from(review)
			.where(
				// review.store.id.eq(condition.getStoreId()),
				// minRating(condition.getMinRating()),
				// maxRating(condition.getMaxRating())
				review.store.id.eq(1L)
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}

	// private BooleanExpression minRating(Long minRating) {
	// 	return minRating != null ? review.rating.goe(minRating) : null;
	// }
	//
	// private BooleanExpression maxRating(Long maxRating) {
	// 	return maxRating != null ? review.rating.loe(maxRating) : null;
	// }
}
