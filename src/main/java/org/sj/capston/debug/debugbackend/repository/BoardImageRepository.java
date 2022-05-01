package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
