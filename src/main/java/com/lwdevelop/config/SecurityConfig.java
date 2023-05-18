package com.lwdevelop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import com.lwdevelop.utils.JwtAuthFilter;

// import com.lwdevelop.backend.service.MemberUserDetailsService;

@Configuration
@EnableWebSecurity // security 過濾器
@EnableGlobalMethodSecurity(prePostEnabled = true) // 方法級別權限驗證
public class SecurityConfig {

    /*
     * @Bean
     * PasswordEncoder passwordEncoder() {
     * return new BCryptPasswordEncoder();
     * }
     */
    // @Autowired
    // AdminServiceImpl adminServiceImpl;

    @Bean
    JwtAuthFilter JwtFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private static final String[] ADMIN_AUTH_LIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/springybot/**",
            
    };

    private static final String[] ALL_AUTH_LIST = {
            "/static/favicon.ico",
            "/static/assets/**",
            "/admins/login",            
            "/index",
            "/version",
            "/jobManagement/**",
            "/groupAndChannelManagement/**",
            "/debug/**",
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                // 基於 token，不需要 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(ADMIN_AUTH_LIST).hasAuthority("ADMIN")
                .antMatchers(ALL_AUTH_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }

}