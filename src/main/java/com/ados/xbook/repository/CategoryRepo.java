package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findFirstBySlug(String slug);
    List<Category> findAllByNameLike(String name);
}
