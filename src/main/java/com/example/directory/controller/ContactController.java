package com.example.directory.controller;

import com.example.directory.dto.ContactDto;
import com.example.directory.mappers.ContactMapper;
import com.example.directory.model.Contact;
import com.example.directory.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
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
    @GetMapping
    public List<ContactDto> getAllContacts(Authentication authentication) {
        List<Contact> contacts = contactService.getContactsByUser(authentication);
        return contacts.stream()
                .map(contactMapper::contactToContactDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@Valid @PathVariable Long id, Authentication authentication) {
        Contact contact = contactService.getContactById(id, authentication);
        ContactDto contactDto = contactMapper.contactToContactDto(contact);
        return new ResponseEntity<>(contactDto, HttpStatus.OK);
    }

    @PostMapping("/kontakt")
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto, Authentication authentication) {
        Contact contact = contactMapper.contactDtoToContact(contactDto);
        Contact createdContact = contactService.createContact(contact, authentication);
        ContactDto createdContactDto = contactMapper.contactToContactDto(createdContact);
        return new ResponseEntity<>(createdContactDto, HttpStatus.CREATED);
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

    @GetMapping("/contacts/grouped")
    public Map<String, List<ContactDto>> getGroupedContacts() {
        return contactService.groupContactsByFirstNameAndLastName();
    }

}
