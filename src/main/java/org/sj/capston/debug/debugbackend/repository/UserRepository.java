package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
