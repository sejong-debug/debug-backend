package org.sj.capston.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.entity.BoardImage;
import org.sj.capston.debug.debugbackend.repository.BoardImageRepository;
import org.sj.capston.debug.debugbackend.util.ImageStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardImageService {

    private final ImageStore imageStore;

    private final BoardImageRepository boardImageRepository;

    public BoardImage getBoardImage(long boardImageId) {
        return boardImageRepository.findById(boardImageId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 boardImageId = " + boardImageId));
    }

    public String getBoardImageFullPath(long boardImageId) {
        BoardImage boardImage = getBoardImage(boardImageId);
        return imageStore.getFullPath(boardImage.getStoredName());
    }
}
