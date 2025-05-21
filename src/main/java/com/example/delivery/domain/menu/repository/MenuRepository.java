package com.example.delivery.domain.menu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.delivery.domain.menu.entity.Menu;

/**
 * Menu Repository
 */
@Repository
public interface MenuRepository extends JpaRepository <Menu, Long>, MenuRepositoryCustom {
	@EntityGraph(attributePaths = {"store", "store.user"})
	Optional<Menu> findMenuById(Long menuId);
}
