package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.SaleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderRepo extends JpaRepository<SaleOrder, Long> {
    SaleOrder findFirstByStatus(Integer status);

    Page<SaleOrder> findAllByCreateByOrderByCreateAtDesc(String username, Pageable pageable);
}
