package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionEntityRepo extends JpaRepository<SessionEntity, String> {
}
