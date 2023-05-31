package com.example.directory.service;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactRepository;
import com.example.directory.repository.FavoriteRepository;
import com.example.directory.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final FavoriteRepository favoriteRepository;

    private final UserAccountRepository userAccountRepository;

    public ContactService(ContactRepository contactRepository, FavoriteRepository favoriteRepository,  UserAccountRepository userAccountRepository) {
        this.contactRepository = contactRepository;
        this.favoriteRepository = favoriteRepository;
        this.userAccountRepository = userAccountRepository;
    }

    //USER

    public UserAccount getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //CONTACTS
    public List<Contact> getContactsByUser(Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        return contactRepository.findByUser(currentUser);
    }

    public Contact getContactById(Long id, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        Optional<Contact> optionalContact = contactRepository.findById(id);
        Contact contact = optionalContact.orElseThrow(RuntimeException::new);
        if (!contact.getUser().equals(currentUser)) {
            throw new RuntimeException("Contact not found contact with id: "+ id);
        }
        return contact;
    }

    public Contact createContact(Contact contact, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        contact.setUser(currentUser);
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, ContactDto contactDto, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        Contact contact = contactRepository.findById(id).orElseThrow(RuntimeException::new);
        contact.setName(contactDto.getName());
        contact.setLastname(contactDto.getLastname());
        contact.setValue(contactDto.getValue());
        contact.setContactType(contactDto.getContactType());
        contact.setDateTime(contactDto.getDateTime());
        contact.setUser(currentUser);
        return contactRepository.save(contact);
    }

    public void deleteContactById(Long id) {
        favoriteRepository.deleteById(id);
        contactRepository.deleteById(id);
    }


    public Map<String, List<ContactDto>> groupContactsByFirstNameAndLastName() {
        List<Contact> contacts = contactRepository.findAll();
        Map<String, List<ContactDto>> groupedContacts = new HashMap<>();

        for (Contact contact : contacts) {
            String key = contact.getName() + " " + contact.getLastname();
            ContactDto contactDto = new ContactDto(contact.getName(), contact.getLastname(), contact.getDateTime(), contact.getContactType(), contact.getValue()); // Replace with the appropriate fields of ContactDto
            groupedContacts.computeIfAbsent(key, k -> new ArrayList<>()).add(contactDto);
        }

        return groupedContacts;
    }
}
