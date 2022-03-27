package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
