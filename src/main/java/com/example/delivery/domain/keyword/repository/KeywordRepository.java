package com.example.delivery.domain.keyword.repository;

import com.example.delivery.domain.keyword.entity.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @Query("""
        SELECT k.keyword
        FROM Keyword k
        ORDER BY k.count DESC
    """)
    Page<String> findPopularKeywords(Pageable pageable);

    Optional<Keyword> findByKeyword(String keyword);

}


