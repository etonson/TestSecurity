package com.example.testsecurity.configs;

import com.example.testsecurity.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
    @author : Eton.lin
    @description 建立使用者請求，路徑filter規則
    @date 2025-01-12 下午 02:55
*/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //禁止CSRF（跨站請求偽造）保護。
        http.csrf(AbstractHttpConfigurer::disable)
                //對所有訪問HTTP端點的HttpServletRequest進行限制
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/error/**",
                                "/api/auth/**"
                        )
                        //指定上述匹配規則中的路徑，允許所有用戶訪問，即不需要進行身份驗證。
                        .permitAll()
                        //其他尚未匹配到的路徑都需要身份驗證
                        .anyRequest().authenticated()
                )
                //注意未加入該行，請求不進行驗證，進而導致權限拒絕。
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}