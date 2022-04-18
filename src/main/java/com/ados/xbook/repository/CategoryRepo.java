package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findFirstBySlug(String slug);
    Page<Category> findAllByNameLike(String name, Pageable pageable);
}
