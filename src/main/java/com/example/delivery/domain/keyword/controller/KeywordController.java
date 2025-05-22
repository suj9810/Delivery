package com.example.delivery.domain.keyword.controller;

import com.example.delivery.common.response.PagingResponse;
import com.example.delivery.domain.keyword.dto.response.KeywordResponseDto;
import com.example.delivery.domain.keyword.dto.response.PopularKeywordDto;
import com.example.delivery.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    // 검색 및 로그 저장
    @GetMapping("/v1/keywords/search")
    public ResponseEntity<Page<KeywordResponseDto>> SearchAndSave(@RequestParam String keyword,
        @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(keywordService.searchV1(keyword, pageable));
    }

    // v2
    @GetMapping("/v2/keywords/search")
    public ResponseEntity<PagingResponse<KeywordResponseDto>> searchWithCache(@RequestParam String keyword,
        @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(keywordService.searchV2(keyword, pageable));
    }

    // 인기 검색어 조회
    @GetMapping("/v1/keywords/popular")
    public ResponseEntity<Page<PopularKeywordDto>> getPopularKeywords(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(keywordService.getPopularKeywords(pageable));
    }

    // 인기 검색어 조회 v2
    @GetMapping("/v2/keywords/popular")
    public ResponseEntity<PagingResponse<PopularKeywordDto>> getPopularKeywordsWithCache(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(keywordService.getPopularKeywordsV2(pageable));
    }

}
