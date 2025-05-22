package com.example.delivery.domain.keyword.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// V2 캐싱 통합검색
	// 검색 로그 저장 및 조회 (in memory cache 적용)
	@Transactional(readOnly = true)
	@Cacheable(value = "searchWithCache", key = "#keyword + '::page=' + #pageable.pageNumber + '&size=' + #pageable.pageSize")
	public Page<KeywordResponseDto> searchWithCache(String keyword, Pageable pageable) {
		log.info("Keyword Search Cache");

		Page<String> storesName = storeRepository.findByNameContaining(keyword, pageable);

		List<KeywordResponseDto> list = storesName.getContent().stream()
			.map(KeywordResponseDto::new)
			.toList();

		return new PageImpl<>(list, pageable, storesName.getTotalElements());
	}

	@CacheEvict(value = "popularKeywords", allEntries = true) // 검색어 count 수정 시 cache reset
	public void updateKeywordCount(String keyword) {
		Keyword target = keywordRepository.findByKeyword(keyword).orElse(null);

		// 이미 검색 기록이 있으면 count++ 없으면 DB에 로그 저장
		if (target != null) {
			target.increaseCount();
			target.updateTime();
		} else {
			keywordRepository.save(new Keyword(keyword));
		}
	}
}
