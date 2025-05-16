package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.dto.response.StoreIdAndNameResponseDto;
import com.example.delivery.domain.store.entity.QStore;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<StoreIdAndNameResponseDto> findStoreIdAndStoreNameByStoreName(Pageable pageable, String storeName) {
        QStore qStore = QStore.store;

        List<StoreIdAndNameResponseDto> dtoList = jpaQueryFactory
                .select(Projections.constructor(StoreIdAndNameResponseDto.class, qStore.id, qStore.storeName))
                .from(qStore)
                .where(hasStoreName(storeName))
                .orderBy(qStore.storeName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory.
                select(qStore.countDistinct())
                .from(qStore)
                .where(hasStoreName(storeName))
                .fetchOne();

        return new PageImpl<>(dtoList, pageable, total);
    }

    private BooleanExpression hasStoreName (String storeName) {
        return storeName != null ? QStore.store.storeName.containsIgnoreCase(storeName) : null;
    }

}
