package com.example.delivery.domain.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.delivery.domain.reviews.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
