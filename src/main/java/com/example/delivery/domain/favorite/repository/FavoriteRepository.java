package com.example.delivery.domain.favorite.repository;

import com.example.delivery.domain.favorite.entity.Favorite;
import com.example.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    //사용자 ID와 가게 ID 로 즐겨찾기 조회
    // 존재 여부 판단, 삭제 시 사용
    Optional<Favorite> findByUserIdAndStoreId(Long userId, Long storeId);

    // 즐겨찾기 존재 여부 확인
    // 중복 방지
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    // 즐겨찾기 삭제
    // 별도 조회 없이 바로 delete
    void deleteByUserIdAndStoreId(Long userId, Long storeId);


}