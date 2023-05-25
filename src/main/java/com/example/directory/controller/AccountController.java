package com.example.directory.controller;

import com.example.directory.model.UserAccount;
import com.example.directory.repository.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    private final UserAccountRepository userAccountRepository;

    public AccountController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/users")
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @GetMapping("/auth")
    public boolean isAuthenticated(HttpServletRequest request) {
        return request.getUserPrincipal() != null;
    }

}
