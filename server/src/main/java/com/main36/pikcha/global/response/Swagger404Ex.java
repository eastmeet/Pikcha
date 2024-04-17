package com.main36.pikcha.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Swagger404Ex {

    @Schema(description = "성공여부입니다.", example = "false")
    private boolean success;
    
    @Schema(description = "성공여부에 대한 메시지입니다.", example = "해당 정보를 찾을 수 없습니다.")
    private String message;

}
