package com.main36.pikcha.global.response;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(implementation = Swagger400Ex.class))
        ),
        @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(implementation = Swagger403Ex.class))
        ),
        @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(implementation = Swagger404Ex.class))
        ),
        @ApiResponse(
                responseCode = "405",
                content = @Content(schema = @Schema(implementation = Swagger405Ex.class))
        ),
        @ApiResponse(
                responseCode = "500",
                content = @Content(schema = @Schema(implementation = Swagger500Ex.class))
        )
})

public @interface SwaggerErrorResponses {
}
