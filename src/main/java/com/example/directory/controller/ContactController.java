package com.example.directory.controller;

import com.example.directory.dto.ContactDto;
import com.example.directory.mappers.ContactMapper;
import com.example.directory.model.Contact;
import com.example.directory.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adresar")
@Validated
public class ContactController {
    private final ContactService contactService;
    private final ContactMapper contactMapper;


    public ContactController(ContactService contactService, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.contactMapper = contactMapper;
    }

    //CONTACTS
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContactDto> getAllContactsAsJson(Authentication authentication) {
        List<Contact> contacts = contactService.getContactsByUser(authentication);
        return contacts.stream()
                .map(contactMapper::contactToContactDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public String showAll(Model model, Authentication authentication) {
        List<Contact> contacts = contactService.getContactsByUser(authentication);
        model.addAttribute("contacts", contacts);
        model.addAttribute("contactDto", new ContactDto());
        return "adresar/adresar";
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@Valid @PathVariable Long id, Authentication authentication) {
        Contact contact = contactService.getContactById(id, authentication);
        ContactDto contactDto = contactMapper.contactToContactDto(contact);
        return new ResponseEntity<>(contactDto, HttpStatus.OK);
    }

//    @PostMapping("/kontakt")
//    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto, Authentication authentication) {
//        Contact createdContact = contactService.createContact(contactDto, authentication);
//        ContactDto createdContactDto = contactMapper.contactToContactDto(createdContact);
//        return new ResponseEntity<>(createdContactDto, HttpStatus.CREATED);
//    }

    @PostMapping("/kontakt")
    public String createContact(@ModelAttribute("contactDto") @Valid ContactDto contactDto,
                                BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "adresar/adresar";
        }

        Contact createdContact = contactService.createContact(contactDto, authentication);
        ContactDto createdContactDto = contactMapper.contactToContactDto(createdContact);
        model.addAttribute("createdContact", createdContactDto);

        List<Contact> contacts = contactService.getContactsByUser(authentication);
        model.addAttribute("contacts", contacts);

        return "adresar/adresar";
    }

    @PutMapping("/detalji/{id}")
    public ResponseEntity<ContactDto> updateContact(@Valid @PathVariable Long id, @RequestBody ContactDto contactDto, Authentication authentication) {
        Contact updatedContact = contactService.updateContact(id, contactDto, authentication);
        ContactDto updatedContactDto = contactMapper.contactToContactDto(updatedContact);
        return new ResponseEntity<>(updatedContactDto, HttpStatus.OK);
    }

    @DeleteMapping("/kontakt/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/kontakt/grouped")
    public Map<String, List<ContactDto>> getGroupedContacts() {
        return contactService.groupContactsByFirstNameAndLastName();
    }

}
