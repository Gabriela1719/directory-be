package com.example.directory.service;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Contact createContact(Contact contact, UserAccount currentUser) {
        contact.setUser(currentUser);
        return contactRepository.save(contact);
    }

    public List<Contact> getContactsByUser(UserAccount currentUser) {
        return contactRepository.findByUser(currentUser);
    }

    public void toggleFavoriteStatus(Contact contact) {
        contact.setFavorite(!contact.isFavorite());
        contactRepository.save(contact);
    }

    public List<Contact> getFavoriteContacts(UserAccount user) {
        return contactRepository.findByUserAndFavorite(user, true);
    }

    public Contact updateContact(Long id, ContactDto contactDto) {
        Contact contact = contactRepository.findById(id).orElseThrow(RuntimeException::new);
        contact.setName(contactDto.getName());
        contact.setLastname(contactDto.getLastname());
        contact.setValue(contactDto.getValue());
        contact.setFavorite(contactDto.isFavorite());
        contact.setContactType(contactDto.getContactType());
        contact.setDateTime(contactDto.getDateTime());
        return contactRepository.save(contact);
    }

    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }
}
