package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> {
    Delivery findFirstByIndex(String index);
}
