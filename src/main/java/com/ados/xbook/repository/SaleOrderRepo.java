package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.SaleOrder;
import com.ados.xbook.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderRepo extends JpaRepository<SaleOrder, Long> {
    SaleOrder findFirstByStatusAndCreateBy(Integer status, String username);

    Page<SaleOrder> findAllByStatusNot(Integer status, Pageable pageable);

    Page<SaleOrder> findAllByCreateByAndStatusNotOrderByCreateAtDesc(String username, Integer status, Pageable pageable);

    Page<SaleOrder> findAllByStatus(Integer status, Pageable pageable);

    Page<SaleOrder> findAllByUserAndStatusNot(User user, Integer status, Pageable pageable);

    Page<SaleOrder> findAllByUserAndStatus(User user, Integer status, Pageable pageable);
}
