package com.example.delivery.domain.menu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.delivery.domain.menu.dto.response.MenuResponse;
import com.example.delivery.domain.menu.entity.QMenu;
import com.example.delivery.domain.store.entity.QStore;
import com.example.delivery.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	QMenu menu = QMenu.menu;
	QStore store = QStore.store;
	QUser user = QUser.user;

	@Override
	public Page<MenuResponse> findMenusByStoreId(Long storeId, Pageable pageable) {
		List<MenuResponse> content = jpaQueryFactory
			.select(Projections.constructor(
				MenuResponse.class,
				menu.id,
				menu.name,
				menu.price,
				menu.description))
			.from(menu)
			.join(menu.store, store)
			.where(menu.store.id.eq(storeId))
			.orderBy(menu.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(menu.count())
			.from(menu)
			.join(menu.store, store)
			.where(menu.store.id.eq(storeId))
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}
}
