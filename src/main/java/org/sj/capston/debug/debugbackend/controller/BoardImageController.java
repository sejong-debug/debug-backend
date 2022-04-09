package org.sj.capston.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.service.BoardImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RequestMapping("/images")
@RestController
@RequiredArgsConstructor
public class BoardImageController {

    private final BoardImageService boardImageService;

    @GetMapping("/{boardImageId}")
    public ResponseEntity<Resource> getBoardImage(
            @PathVariable long boardImageId) throws MalformedURLException {
        UrlResource resource = new UrlResource("file:" + boardImageService.getBoardImageFullPath(boardImageId));
        return ResponseEntity.ok(resource);
    }
}
