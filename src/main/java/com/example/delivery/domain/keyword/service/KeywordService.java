package com.example.delivery.domain.keyword.service;

import com.example.delivery.common.response.PagingResponse;
import com.example.delivery.domain.keyword.dto.response.KeywordResponseDto;
import com.example.delivery.domain.keyword.dto.response.PopularKeywordDto;
import com.example.delivery.domain.keyword.entity.Keyword;
import com.example.delivery.domain.keyword.repository.KeywordRepository;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final KeywordCacheService keywordCacheService;
    private final StoreRepository storeRepository;

    private final RedisTemplate<String, Object> redisTemplateObject;

    // 검색 로그 저장 및 조회 (캐시 미적용)
    public Page<KeywordResponseDto> searchV1(String keyword, Pageable pageable) {
        // 보여줄 페이지 생성
        Page<String> storesName = storeRepository.findByNameContaining(keyword, pageable);

        List<KeywordResponseDto> list = storesName.getContent().stream()
            .map(KeywordResponseDto::new)
            .toList();

        Page<KeywordResponseDto> page = new PageImpl<>(list, pageable, storesName.getTotalElements());

        // 이미 검색 기록이 있으면 count++ 없으면 DB에 로그 저장
        Keyword target = keywordRepository.findByKeyword(keyword).orElse(null);

        if (target != null) {
            target.increaseCount();
            target.updateTime();
        } else {
            keywordRepository.save(new Keyword(keyword));
        }
        return page;
    }

    // 인기 검색어 조회
    @Transactional(readOnly = true)
    public Page<PopularKeywordDto> getPopularKeywords(Pageable pageable) {

        AtomicInteger counter = new AtomicInteger(1);
        Page<String> keywords = keywordRepository.findPopularKeywords(pageable);

        List<PopularKeywordDto> list = keywords.getContent().stream()
            .map(keyword -> new PopularKeywordDto(counter.getAndIncrement(), keyword))
            .toList();

        Page<PopularKeywordDto> page = new PageImpl<>(list, pageable, keywords.getTotalElements());
        return page;
    }

    public PagingResponse<KeywordResponseDto> searchV2(String keyword, Pageable pageable) {
        PagingResponse<KeywordResponseDto> result = keywordCacheService.searchWithCache(keyword, pageable); // 캐시 적용 된 조회
        keywordCacheService.updateKeywordCount(keyword);
        return result;
    }

    // 인기 검색어 조회
    @Transactional(readOnly = true)
    public PagingResponse<PopularKeywordDto> getPopularKeywordsV2(Pageable pageable) {
        String cacheKey = "popularKeywords::page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();

        PagingResponse<PopularKeywordDto> cacheData = (PagingResponse<PopularKeywordDto>)redisTemplateObject.opsForValue().get(cacheKey);
        if (cacheData != null) {
            return cacheData;
        }

        log.info("Popular Keyword Cache");

        // Cache miss
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;
        AtomicInteger counter = new AtomicInteger(startRank);

        Page<String> keywords = keywordRepository.findPopularKeywords(pageable);
        List<PopularKeywordDto> list = keywords.getContent().stream()
            .map(keyword -> new PopularKeywordDto(counter.getAndIncrement(), keyword))
            .toList();

        Page<PopularKeywordDto> page = new PageImpl<>(list, pageable, keywords.getTotalElements());
        PagingResponse<PopularKeywordDto> result = PagingResponse.from(page);

        redisTemplateObject.opsForValue().set(cacheKey, result, Duration.ofMinutes(1));
        return result;
    }
}