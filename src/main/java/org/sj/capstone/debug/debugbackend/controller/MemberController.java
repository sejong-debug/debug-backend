package org.sj.capstone.debug.debugbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.common.ApiResult;
import org.sj.capstone.debug.debugbackend.dto.member.MemberJoinDto;
import org.sj.capstone.debug.debugbackend.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResult<Void>> join(@Valid @RequestBody MemberJoinDto memberJoinDto) {
        memberService.join(memberJoinDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResult.<Void>builder().build());
    }

    @GetMapping("/exist/username")
    public ResponseEntity<ApiResult<Boolean>> checkDuplicateUsername(@RequestParam String username) {
        ApiResult<Boolean> result = ApiResult.<Boolean>builder()
                .data(memberService.checkDuplicateUsername(username))
                .build();
        return ResponseEntity.ok(result);
    }
}
