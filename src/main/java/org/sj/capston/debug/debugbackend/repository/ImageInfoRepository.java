package org.sj.capston.debug.debugbackend.repository;

import org.sj.capston.debug.debugbackend.entity.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {
}
