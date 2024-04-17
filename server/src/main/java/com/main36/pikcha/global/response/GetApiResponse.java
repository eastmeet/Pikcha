package com.main36.pikcha.global.response;


import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "200",
        useReturnTypeSchema = true
)
public @interface GetApiResponse {
}
