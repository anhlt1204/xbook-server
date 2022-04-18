package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.Category;
import com.ados.xbook.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Product findFirstBySlug(String slug);

    Page<Product> findAllByTitleLike(String title, Pageable pageable);

    Page<Product> findAllByCategoryAndTitleLike(Category category, String title, Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

}
