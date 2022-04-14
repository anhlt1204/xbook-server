package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderRepo extends JpaRepository<SaleOrder, Long> {
}
