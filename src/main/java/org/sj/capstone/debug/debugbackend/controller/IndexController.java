package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.security.MemberContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<Void> index(@AuthenticationPrincipal MemberContext memberContext) {
        return ResponseEntity.noContent().build();
    }
}
