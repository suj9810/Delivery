package com.example.delivery.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "store")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String storeName;

    private String address;

    private String phoneNumber;

    private Long minOrderPrice;

    private LocalTime openTime;

    private LocalTime closedTime;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    // 가게 수정
    public void update(String storeName,
                       String address,
                       String phoneNumber,
                       LocalTime openTime,
                       LocalTime closedTime,
                       Long minOrderPrice
    ) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
    }
}
