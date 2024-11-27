package com.trollyworld.bootapp.repository;

import com.trollyworld.bootapp.model.Category;
import com.trollyworld.bootapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepositpory extends JpaRepository<Product, Long> {
    Optional<Product> findByPrdName(String prdName);
    Page<Product> findByCategory(Category category, Pageable pageRequest);
    Page<Product> findByPrdNameLikeIgnoreCase(String searchTerm, Pageable pageRequest);
}
