package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    @EntityGraph(attributePaths = {"user"})
    Optional<Store> findStoreAndUserById (Long storeId);
}
