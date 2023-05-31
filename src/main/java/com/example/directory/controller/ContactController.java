package com.example.directory.controller;

import com.example.directory.dto.ContactDto;
import com.example.directory.mappers.ContactMapper;
import com.example.directory.model.Contact;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.UserAccountRepository;
import com.example.directory.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adresar")
@Validated
public class ContactController {
    private final ContactService contactService;
    private final UserAccountRepository userAccountRepository;
    private final ContactMapper contactMapper;


    public ContactController(ContactService contactService, UserAccountRepository userAccountRepository, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.userAccountRepository = userAccountRepository;
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
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        UserAccount currentUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<Contact> optionalContact = contactService.getContactById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            if (contact.getUser().equals(currentUser)) {
                ContactDto contactDto = contactMapper.contactToContactDto(contact);
                return new ResponseEntity<>(contactDto, HttpStatus.OK);
            }
        }
        throw new UsernameNotFoundException("Cannot find contact with Id: " + id);
    }

    @PostMapping("/kontakt")
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        UserAccount currentUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Contact contact = contactMapper.contactDtoToContact(contactDto);
        contact.setUser(currentUser);
        Contact createdContact = contactService.createContact(contact, currentUser);
        ContactDto createdContactDto = contactMapper.contactToContactDto(createdContact);
        return new ResponseEntity<>(createdContactDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/favorite")
    public ResponseEntity<Void> toggleFavoriteStatus(@Valid @PathVariable Long id, Authentication authentication, Favorite favorite) {
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        UserAccount currentUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("Contact not found"));

        Optional<Contact> optionalContact = contactService.getContactById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            if (contact.getUser().equals(currentUser)) {
                contactService.toggleFavoriteStatus(contact);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new UsernameNotFoundException("Cannot find contact with Id: " + id);
        }
    }

    @DeleteMapping("/kontakt/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }

    //FAVORITE

    @GetMapping("/omiljeni")
    public List<Favorite> getFavoriteContacts(Authentication authentication) {
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        UserAccount currentUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return contactService.getFavoriteContacts(currentUser);
    }

    @PutMapping("/detalji/{id}")
    public ResponseEntity<Contact> updateContact(@Valid @PathVariable Long id, @RequestBody ContactDto contactDto) {
        Contact updatedContact = contactService.updateContact(id, contactDto);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

}
