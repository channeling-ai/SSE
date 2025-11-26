package channeling.sse.global.config;

import channeling.sse.global.auth.application.CustomUserDetailsService;
import channeling.sse.global.auth.filter.AuthenticationEntryPointImpl;
import channeling.sse.global.auth.filter.JwtAuthenticationProcessingFilter;
import channeling.sse.global.infrastructure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // TODO 커스텀 필터 등록 (자체 JWT 인가 필터, 예외처리 필터 등)
                //미인증 상태로, whiteList 에 접근했을 경우
                .addFilterAfter(jwtAuthenticationProcessingFilter(), ExceptionTranslationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()))
//                // TODO 엔드포인트 추가 (개발 후)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/test").authenticated()
                        .anyRequest().permitAll()
                )
        ;


        return http.build();
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    // TODO JWT 인증 필터
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://channeling.it.com", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

