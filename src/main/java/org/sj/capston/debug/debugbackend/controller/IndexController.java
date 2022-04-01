package org.sj.capston.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.auth.PrincipalDetails;
import org.sj.capston.debug.debugbackend.dto.JoinDto;
import org.sj.capston.debug.debugbackend.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinDto joinDto) {
        memberService.join(joinDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<Void> index(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().build();
    }
}
