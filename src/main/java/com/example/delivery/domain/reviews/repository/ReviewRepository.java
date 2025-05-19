package com.example.delivery.domain.reviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.delivery.domain.User.entity.User;
import com.example.delivery.domain.reviews.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
	Page<Review> findByStoreId(Long storeId, Pageable pageable);

	Long user(User user);
}
