package com.example.delivery.domain.reviews.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Integer rating;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public void update(String content) {
		this.content = content;
	}

	// public void validateOwner(User user) {
	// 	if (!user.getId().equals(this.getUser().getId())) {
	// 		throw new ReviewException(ReviewExceptionCode.NOT_OWNER_OF_REVIEW);
	// 	}
	// }

}
