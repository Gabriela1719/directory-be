package com.example.directory.controller;

import com.example.directory.dto.UserAccountDto;
import com.example.directory.exception.EmailAlreadyExistsException;
import com.example.directory.mappers.UserAccountMapper;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Validated
public class AuthController {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountMapper userAccountMapper;

    public AuthController(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccountMapper = userAccountMapper;
    }

    @PostMapping("/register")
    public UserAccount registerUser(@Valid @RequestBody UserAccountDto userAccountDto) {

        UserAccount userAccount = userAccountMapper.userDtoToUser(userAccountDto);

        String email = userAccount.getEmail();
        if(userAccountRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String hashPwd = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(hashPwd);

        return userAccountRepository.save(userAccount);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        response.sendRedirect("/login?logout=true");
    }

//    @PostMapping("/login")
//    public void login(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/adresar");
//    }

    @GetMapping("/auth")
    public boolean isAuthenticated(HttpServletRequest request) {
        return request.getUserPrincipal() != null;
    }

}
