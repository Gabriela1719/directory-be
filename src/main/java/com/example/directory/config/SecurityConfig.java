package com.example.directory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
