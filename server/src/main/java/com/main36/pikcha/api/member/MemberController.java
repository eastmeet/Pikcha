package com.main36.pikcha.api.member;


import com.main36.pikcha.domain.member.dto.MemberDto;
import com.main36.pikcha.domain.member.dto.MemberResponseDto;
import com.main36.pikcha.domain.member.entity.Member;
import com.main36.pikcha.domain.member.mapper.MemberMapper;
import com.main36.pikcha.domain.member.service.MemberService;
import com.main36.pikcha.global.aop.LoginUser;
import com.main36.pikcha.global.response.*;
import com.main36.pikcha.global.utils.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "[Member] 멤버", description = "멤버 관련 API 입니다.")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final CookieUtils cookieUtils;

    @Operation(summary = "사용자 등록")
    @PostMapping("/signup")
    @PostApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> postMember(@Valid @RequestBody MemberDto.Post memberPostDto) {
        Member member = mapper.memberPostDtoToMember(memberPostDto);

        Member createMember = memberService.createMember(member);

        return new ResponseEntity<>(
                new DataResponseDto<>(mapper.memberToSignUpResponseDto(createMember)),
                HttpStatus.CREATED);
    }

    @LoginUser
    @Operation(summary = "사용자 정보 수정")
    @PatchMapping("/users/edit/{member-id}")
    @PatchApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> patchMember(Member loginUser,
                                                          @PathVariable("member-id") @Positive Long memberId,
                                                          @Valid @RequestBody MemberDto.Patch memberPatchDto) {

        memberService.verifyLoginIdAndMemberId(loginUser.getMemberId(), memberId);
        memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchDtoToMember(memberPatchDto));

        return ResponseEntity.ok(new DataResponseDto<>(mapper.memberToProfileHomeDto(member)));
    }

    @LoginUser
    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/users/profile/{member-id}")
    @GetApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<MemberResponseDto.Profile>> getMemberProfile(Member loginUser,
                                                                                       @PathVariable("member-id") @Positive Long memberId) {
        Member member = memberService.verifyLoginIdAndMemberId(loginUser.getMemberId(), memberId);
        return ResponseEntity.ok(new DataResponseDto<>(mapper.memberToProfileHomeDto(member)));
    }

    @LoginUser
    @Operation(summary = "사용자 탈퇴")
    @DeleteMapping("/users/delete/{member-id}")
    @DeleteApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<Object> deleteMember(Member loginUser,
                                                   @PathVariable("member-id") @Positive Long memberId) {

        Member member = memberService.verifyLoginIdAndMemberId(loginUser.getMemberId(), memberId);
        memberService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }

}
