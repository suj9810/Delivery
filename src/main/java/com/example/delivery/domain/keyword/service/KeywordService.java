package com.example.delivery.domain.keyword.service;

import com.example.delivery.domain.keyword.dto.response.KeywordResponseDto;
import com.example.delivery.domain.keyword.dto.response.PopularKeywordDto;
import com.example.delivery.domain.keyword.entity.Keyword;
import com.example.delivery.domain.keyword.repository.KeywordRepository;
import com.example.delivery.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<KeywordResponseDto> searchV2(String keyword, Pageable pageable) {
        Page<KeywordResponseDto> result = keywordCacheService.searchWithCache(keyword, pageable); // 캐시 적용 된 조회
        keywordCacheService.updateKeywordCount(keyword);
        return result;
    }

    // 인기 검색어 조회
    @Transactional(readOnly = true)
    @Cacheable(value = "popularKeywords", key = "'result::page=' + #pageable.pageNumber + '&size=' + #pageable.pageSize")
    public Page<PopularKeywordDto> getPopularKeywordsV2(Pageable pageable) {
        log.info("Popular Keyword  Cache");
        AtomicInteger counter = new AtomicInteger(1);
        Page<String> keywords = keywordRepository.findPopularKeywords(pageable);

        List<PopularKeywordDto> list = keywords.getContent().stream()
            .map(keyword -> new PopularKeywordDto(counter.getAndIncrement(), keyword))
            .toList();

        Page<PopularKeywordDto> page = new PageImpl<>(list, pageable, keywords.getTotalElements());
        return page;
    }
}