package com.ados.xbook.repository;

import com.ados.xbook.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findFirstByUsername(String username);

    Page<User> findAllByUsernameLike(String username, Pageable pageable);
}
