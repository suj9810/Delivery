package com.example.delivery.domain.favorite.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.favorite.dto.FavoriteStoreDto;
import com.example.delivery.domain.favorite.entity.Favorite;
import com.example.delivery.domain.favorite.repository.FavoriteQueryRepository;
import com.example.delivery.domain.favorite.repository.FavoriteRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteQueryRepository favoriteQueryRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 즐겨찾기 등록, 이미 등록되어있으면 예외 발생
    @CacheEvict(value = "favoriteList", key = "#userId")
    public void addFavorite(Long userId, Long storeId) {
        if (favoriteRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new CustomException(ErrorCode.ALREADY_FAVORITE);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        favoriteRepository.save(Favorite.of(user, store));
    }

    // 즐겨찾기 삭제
    @CacheEvict(value = "favoriteList", key = "#userId")
    public void deleteFavorite(Long userId, Long storeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.deleteByUserIdAndStoreId(userId, storeId);
    }

    // 사용자의 즐겨찾기 가게 목록 조회
    @Cacheable(value = "favoriteList", key = "#userId")
    public List<FavoriteStoreDto> getMyFavorites(Long userId) {
        return favoriteQueryRepository.findFavoriteStoreByUserId(userId);
    }

    // 인증된 사용자와 요청 경로상의 userId 가 일치하는지 검증
    public void validateUser(Long pathUserId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            if (!userDetails.getId().equals(pathUserId)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ROLE);
            }
            return;
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED_ROLE);
    }

}
