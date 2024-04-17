package com.main36.pikcha.api.attraction;


import com.main36.pikcha.domain.attraction.dto.AttractionPatchDto;
import com.main36.pikcha.domain.attraction.dto.AttractionPostDto;
import com.main36.pikcha.domain.attraction.dto.AttractionResponseDto;
import com.main36.pikcha.domain.attraction.entity.Attraction;
import com.main36.pikcha.domain.attraction.mapper.AttractionMapper;
import com.main36.pikcha.domain.attraction.service.AttractionService;
import com.main36.pikcha.domain.member.service.MemberService;
import com.main36.pikcha.global.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
@Validated
@Tag(name = "[관리자] Attraction", description = "명소 관련 관리자 API 입니다.")
public class AdminAttractionController {

    private final AttractionService attractionService;

    private final AttractionMapper mapper;

    private final MemberService memberService;

    @Operation(summary = "관리자 페이지(명소 등록)")
    @PostMapping("/upload")
    @PostApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> postAttraction(AttractionPostDto attractionPostDto) {

        Attraction attraction = mapper.attractionPostDtoToAttraction(attractionPostDto);
        AttractionResponseDto response =
                mapper.attractionToAttractionResponseDto(attractionService.createAttraction(attraction));
        return ResponseEntity.ok(new DataResponseDto<>(response));
    }

    @Operation(summary = "관리자 페이지(명소 수정)")
    @PatchMapping("/edit/{attraction-id}")
    @PatchApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<DataResponseDto<?>> patchAttraction(@PathVariable("attraction-id") @Positive long attractionId,
                                                              AttractionPatchDto patchDto) {
        patchDto.setAttractionId(attractionId);
        Attraction attraction = mapper.attractionPatchDtoToAttraction(patchDto);
        AttractionResponseDto response =
                mapper.attractionToAttractionResponseDto(attractionService.updateAttraction(attraction));
        return ResponseEntity.ok(new DataResponseDto<>(response));
    }

    @Operation(summary = "관리자페이지(명소 삭제)")
    @DeleteMapping("/delete/{attraction-id}")
    @DeleteApiResponse
    @SwaggerErrorResponses
    public ResponseEntity<Object> deleteAttraction(@PathVariable("attraction-id") @Positive long attractionId) {
        attractionService.deleteAttraction(attractionId);
        return ResponseEntity.noContent().build();
    }

}
