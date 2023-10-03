package com.example.directory.controller;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.Favorite;
import com.example.directory.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
@Validated
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PutMapping("/{id}/favorite")
    public ResponseEntity<Void> toggleFavoriteStatus(@Valid @PathVariable Long id, Authentication authentication) {
        favoriteService.toggleFavoriteStatus(id, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/omiljeni")
    public String getFavoriteContacts(Model model, Authentication authentication) {
        List<Favorite> favoriteList = favoriteService.getFavoriteContacts(authentication);
        model.addAttribute("contacts", favoriteList);
        model.addAttribute("contactDto", new ContactDto());
        return "adresar/favorite";
    }

}
