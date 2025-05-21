package com.example.delivery.domain.keyword.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "keywords")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
     private String keyword;

    @Column(nullable = false)
    private int count;

    @Column(name = "last_searched_at", nullable = false)
    private LocalDateTime updatedAt;

    public Keyword(String keyword) {
        this.keyword = keyword;
        this.count = 0;
    }

    public void increaseCount() {
        this.count++;
    }

    public void updateTime() {
        this.updatedAt = LocalDateTime.now();
    }


}
