package com.example.delivery.domain.keyword.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.keyword.dto.response.KeywordResponseDto;
import com.example.delivery.domain.keyword.dto.response.PopularKeywordDto;
import com.example.delivery.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<KeywordResponseDto>> SearchAndSave(@RequestParam String keyword,
                                                                  @PageableDefault(size = 10, page = 0) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.CREATED).body(keywordService.SearchAndSave(keyword, pageable));
    }

    // 인기 검색어 조회
    public ResponseEntity<Page<PopularKeywordDto>> getPopularKeywords(@PageableDefault(size = 10, page = 0) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.CREATED).body(keywordService.getPopularKeywords(pageable));
    }

}
