package org.springybot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springybot.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findById(Integer id);

    Admin findByUsername(String username);

    Page<Admin> findByUsername(String username, Pageable pageable);

    Page<Admin> findAllByUsernameContaining(String username, Pageable pageable);
}
