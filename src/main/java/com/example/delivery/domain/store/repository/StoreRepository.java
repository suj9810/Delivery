package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    @EntityGraph(attributePaths = {"user"})
    Optional<Store> findStoreAndUserById (Long storeId);

    @Query("""
    SELECT s.storeName
    FROM Store s
    WHERE s.storeName LIKE %:keyword%
    ORDER BY s.storeName ASC
    """)
    Page<String> findByNameContaining(String keyword, Pageable pageable);

}
