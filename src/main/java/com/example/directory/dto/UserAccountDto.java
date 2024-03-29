package com.example.directory.dto;

import com.example.directory.validation.ValidPassword;
import com.example.directory.validation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserAccountDto {
    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    @NotEmpty(message = "Email is required.")
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserAccountDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
