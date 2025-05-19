package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.dto.response.StoreIdAndNameResponseDto;
import com.example.delivery.domain.store.entity.QStore;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.delivery.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<StoreIdAndNameResponseDto> findStoreIdAndStoreNameByStoreName(Pageable pageable, String storeName) {

        List<StoreIdAndNameResponseDto> dtoList = jpaQueryFactory
                .select(Projections.constructor(StoreIdAndNameResponseDto.class, store.id, store.storeName))
                .from(store)
                .where(hasStoreName(storeName))
                .orderBy(store.storeName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.
                select(store.countDistinct())
                .from(store)
                .where(hasStoreName(storeName))
                .fetchOne();

        return new PageImpl<>(dtoList, pageable, total);
    }

    private BooleanExpression hasStoreName (String storeName) {
        return storeName != null ? store.storeName.containsIgnoreCase(storeName) : null;
    }

}
