package com.main36.pikcha.global.security;

import com.main36.pikcha.domain.member.service.MemberService;
import com.main36.pikcha.global.security.filter.JwtAuthenticationFilter;
import com.main36.pikcha.global.security.filter.JwtVerificationFilter;
import com.main36.pikcha.global.security.jwt.JwtGenerator;
import com.main36.pikcha.global.security.handler.*;
import com.main36.pikcha.global.security.jwt.JwtParser;
import com.main36.pikcha.global.security.oauth.OAuth2MemberSuccessHandler;
import com.main36.pikcha.global.security.oauth.OauthService;
import com.main36.pikcha.global.utils.CookieUtils;
import com.main36.pikcha.global.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtParser jwtParser;
    private final JwtGenerator jwtGenerator;
    private final MemberService memberService;
    private final OauthService oauthService;
    private final CookieUtils cookieUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .headers().frameOptions().sameOrigin()

                .and()
                .cors().configurationSource(corsConfigurationSource())

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())

                .and()
                .apply(new CustomFilterConfigure())

                .and()
                .logout()
                .deleteCookies("refreshToken")
                .logoutSuccessHandler(new MemberLogoutSuccessHandler())
                .logoutSuccessUrl("/")

                .and()
                .authorizeHttpRequests(authorize -> authorize
////						.requestMatchers(toH2Console()).permitAll()
                        .antMatchers("/attractions/upload", "/attractions/edit/**", "/attractions/delete", "admin").hasRole("ADMIN")
                        .anyRequest().permitAll())

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2MemberSuccessHandler(memberService, jwtGenerator, cookieUtils))
                        .userInfoEndpoint()
                        .userService(oauthService));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000", "http://pikcha.kr/", "https://pikcha.kr/"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    public class CustomFilterConfigure extends AbstractHttpConfigurer<CustomFilterConfigure, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtGenerator, authenticationManager, cookieUtils);
            jwtAuthenticationFilter.setFilterProcessesUrl("/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtGenerator, jwtParser);

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);

        }
    }
}

