package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Slice<Board> findAllByProjectId(Pageable pageable, long projectId);
}
