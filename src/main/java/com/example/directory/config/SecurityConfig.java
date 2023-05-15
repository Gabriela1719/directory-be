package com.example.directory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/adresar/kontakt").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/adresar/{id}/favorite", "/adresar/detalji/{id}").authenticated()
                    .requestMatchers(HttpMethod.GET, "/adresar", "/adresar/{id}", "/adresar/omiljeni", "/profile").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/adresar/kontakt/{id}").authenticated()
                    .requestMatchers("/register").permitAll()
                .and()
                .formLogin().defaultSuccessUrl("/adresar", true)
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/login?expired=true")
                .and()
                .sessionFixation().newSession();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
