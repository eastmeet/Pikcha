package com.main36.pikcha.global.utils;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {

    public static final String SET_COOKIE = "Set-Cookie";
    public void setCookieInHeader(HttpServletResponse response, String refreshToken) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(3 * 24 * 60 * 60) // 쿠키 유효기간 설정 (3일)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
//                .domain(".pikcha36.o-r.kr") // 성공 -> 할때 sameSite 제거
//                .domain("pikcha36.o-r.kr") // 성공 -> 할때 sameSite 제거
                .build();

        response.setHeader(SET_COOKIE, String.valueOf(cookie));
    }

    public static void setCookie(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(3 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // 문제점!
        response.addCookie(cookie);
    }

    public String getCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies(); // 모든 쿠키 가져오기
        if (cookies != null) {
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("refreshToken")) {
                    return value;
                }
            }
        }
        return null;
    }

    public void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0) // 쿠키 유효기간 설정 (3일)
                .build();

        response.setHeader(SET_COOKIE, String.valueOf(cookie));
    }

}
