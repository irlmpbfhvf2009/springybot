package org.springybot.config;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springybot.utils.JwtAuthFilter;

// import springfox.documentation.oas.annotations.EnableOpenApi;

// import com.lwdevelop.backend.service.MemberUserDetailsService;

// @EnableOpenApi
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

    // private static final String[] ADMIN_AUTH_LIST = {

    // };

    private static final String[] ALL_AUTH_LIST = {
            "/invitationBonusUser/**",
            "/groupAndChannelManagement/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/static/favicon.ico",
            "/static/assets/**",
            "/admins/v1/login",
            "/debug/**",
            "/callback/**",
            "/redis/**",
            "/index/**",
            "/springybot/v1/cacheSpringyBotDataToRedis",
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
                .antMatchers(ALL_AUTH_LIST).permitAll()
                // .antMatchers(ADMIN_AUTH_LIST).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5489")); // 允許來自PORT 5488的請求
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}