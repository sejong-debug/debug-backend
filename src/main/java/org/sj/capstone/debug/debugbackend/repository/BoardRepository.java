package org.sj.capstone.debug.debugbackend.repository;

import org.sj.capstone.debug.debugbackend.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Slice<Board> findAllByProjectId(Pageable pageable, long projectId);

    boolean existsByIdAndProjectId(long id, long projectId);
}
