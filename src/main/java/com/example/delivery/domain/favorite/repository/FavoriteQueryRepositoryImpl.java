package com.example.delivery.domain.favorite.repository;

import com.example.delivery.domain.favorite.dto.FavoriteStoreDto;
import com.example.delivery.domain.favorite.entity.QFavorite;
import com.example.delivery.domain.store.entity.QStore;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteQueryRepositoryImpl implements FavoriteQueryRepository {
    private final JPAQueryFactory queryFactory;

    // 즐겨찾기 된 가게 dto 리스트 조회
    // 필요한 필드만 조회되도록 함
    @Override
    public List<FavoriteStoreDto> findFavoriteStoreByUserId(Long userId) {
        QFavorite favorite = QFavorite.favorite;
        QStore store = QStore.store;

        return queryFactory
                .select(Projections.constructor(FavoriteStoreDto.class,
                        store.id,
                        store.storeName,
                        store.address,
                        store.minOrderPrice,
                        store.openTime,
                        store.closedTime))
                .from(favorite)
                .join(favorite.store, store)
                .where(favorite.user.id.eq(userId))
                .fetch();

    }

}