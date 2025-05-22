package com.example.delivery.domain.keyword.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.response.PagingResponse;
import com.example.delivery.domain.keyword.dto.response.KeywordResponseDto;
import com.example.delivery.domain.keyword.entity.Keyword;
import com.example.delivery.domain.keyword.repository.KeywordRepository;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KeywordCacheService {

	private final StoreRepository storeRepository;

	private final KeywordRepository keywordRepository;
	private final RedisTemplate<String, Object> redisTemplateObject;

	// V2 캐싱 통합검색
	// 검색 로그 저장 및 조회 (redis 적용)
	@Transactional(readOnly = true)
	public PagingResponse<KeywordResponseDto> searchWithCache(String keyword, Pageable pageable) {
		String cacheKey = "keyword::" + keyword + "::page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

		PagingResponse<KeywordResponseDto> cacheData = (PagingResponse<KeywordResponseDto>)redisTemplateObject.opsForValue().get(cacheKey);
		if (cacheData != null) {
			return cacheData;
		}

		log.info("Keyword Search Cache");
		// Cache miss
		Page<String> storesName = storeRepository.findByNameContaining(keyword, pageable);
		List<KeywordResponseDto> list = storesName.getContent().stream()
			.map(KeywordResponseDto::new)
			.toList();

		Page<KeywordResponseDto> page = new PageImpl<>(list, pageable, storesName.getTotalElements());
		PagingResponse<KeywordResponseDto> result = PagingResponse.from(page);

		redisTemplateObject.opsForValue().set(cacheKey, result, Duration.ofMinutes(10));

		return result;
	}

	public void updateKeywordCount(String keyword) {
		Keyword target = keywordRepository.findByKeyword(keyword).orElse(null);

		// 이미 검색 기록이 있으면 count++ 없으면 DB에 로그 저장
		if (target != null) {
			target.increaseCount();
			target.updateTime();
		} else {
			keywordRepository.save(new Keyword(keyword));
		}

		// 인기 검색어 관련 cache 초기화
		Set<String> keys = redisTemplateObject.keys("popularKeywords::*");
		if (keys != null && keys.size() > 0) {
			redisTemplateObject.delete(keys);
		}
	}
}
