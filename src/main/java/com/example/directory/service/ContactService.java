package com.example.directory.service;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.ContactType;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactRepository;
import com.example.directory.repository.FavoriteRepository;
import com.example.directory.repository.UserAccountRepository;
import com.example.directory.validation.ValueValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserAccountRepository userAccountRepository;

    public ContactService(ContactRepository contactRepository, FavoriteRepository favoriteRepository, UserAccountRepository userAccountRepository) {
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

    public Contact createContact(ContactDto contactDto, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        validateContactDto(contactDto);

        String contactValue = contactDto.getValue();
        Contact existingContact = contactRepository.findByValueAndUser(contactValue, currentUser);

        if (existingContact != null) {
            throw new RuntimeException("Value already exists");
        }
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setLastname(contactDto.getLastname());
        contact.setCountry(contactDto.getCountry());
        contact.setContactType(contactDto.getContactType());
        contact.setValue(contactDto.getValue());
        contact.setUser(currentUser);
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, ContactDto contactDto, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        validateContactDto(contactDto);
        Contact contact = contactRepository.findById(id).orElseThrow(RuntimeException::new);
        contact.setName(contactDto.getName());
        contact.setLastname(contactDto.getLastname());
        contact.setValue(contactDto.getValue());
        contact.setContactType(contactDto.getContactType());
        contact.setCountry(contactDto.getCountry());
        contact.setUser(currentUser);

        boolean isFavorite = contact.isFavorite();
        contact.setFavorite(!isFavorite);

        Favorite existingFavorite = favoriteRepository.findByContact(contact);
        if (existingFavorite != null) {
            existingFavorite.setName(contact.getName());
            existingFavorite.setLastname(contact.getLastname());
            existingFavorite.setContactType(contact.getContactType());
            existingFavorite.setValue(contact.getValue());
            existingFavorite.setCountry(contact.getCountry());
            existingFavorite.setUser(contact.getUser());
            existingFavorite.setContact(contact);

            favoriteRepository.save(existingFavorite);
        }

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
            ContactDto contactDto = new ContactDto(contact.getName(), contact.getLastname(), contact.getCountry(), contact.getContactType(), contact.getValue()); // Replace with the appropriate fields of ContactDto
            groupedContacts.computeIfAbsent(key, k -> new ArrayList<>()).add(contactDto);
        }

        return groupedContacts;
    }

    private void validateContactDto(ContactDto contactDto) {
        ContactType contactType = contactDto.getContactType();
        String value = contactDto.getValue();

        if (contactType == ContactType.EMAIL) {
            ValueValidator.validateEmail(value);
        } else if (contactType == ContactType.MOBITEL) {
            ValueValidator.validateMobileNumber(value);
        } else if (contactType == ContactType.FIXNI_TELEFON) {
            ValueValidator.validateLandlineNumber(value);
        }
    }
}
