package com.main36.pikcha.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Swagger400Ex {

    @Schema(description = "성공여부입니다.", example = "false")
    private boolean success;

    @Schema(description = "400 에러 메시지입니다.", example = "잘못된 요청 혹은 유효하지 않은 요청입니다.")
    private String message;
}
