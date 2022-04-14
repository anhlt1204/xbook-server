package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.Product;
import com.ados.xbook.domain.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProduct(Product product);
}
