package com.example.directory.controller;

import com.example.directory.dto.UserAccountDto;
import com.example.directory.exception.EmailAlreadyExistsException;
import com.example.directory.mappers.UserAccountMapper;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

@Controller
@EnableWebMvc
@Validated
public class AuthController {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountMapper userAccountMapper;

    public AuthController(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, UserAccountMapper userAccountMapper, UserAccountMapper userAccountMapper1) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccountMapper = userAccountMapper;
    }

    @PostMapping(value =  "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccount registerUserJson(@Valid @RequestBody UserAccountDto userAccountDto) {

        UserAccount userAccount = userAccountMapper.userDtoToUser(userAccountDto);
        String email = userAccount.getEmail();
        if (userAccountRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        String hashPwd = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(hashPwd);
        return userAccountRepository.save(userAccount);
    }

    @PostMapping(value =  "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String registerUserHtml(@Valid @ModelAttribute UserAccountDto userAccountDto, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            UserAccount userAccount = userAccountMapper.userDtoToUser(userAccountDto);
            String email = userAccount.getEmail();
            if (userAccountRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            String hashPwd = passwordEncoder.encode(userAccount.getPassword());
            userAccount.setPassword(hashPwd);
            userAccountRepository.save(userAccount);

            model.addAttribute("successMessage", "Registration successful! You can now log in.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }


    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        response.sendRedirect("/login?logout=true");
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/auth")
    public boolean isAuthenticated(HttpServletRequest request) {
        return request.getUserPrincipal() != null;
    }

}
