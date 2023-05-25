package com.example.directory.service;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactRepository;
import com.example.directory.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final FavoriteRepository favoriteRepository;

    public ContactService(ContactRepository contactRepository, FavoriteRepository favoriteRepository) {
        this.contactRepository = contactRepository;
        this.favoriteRepository = favoriteRepository;
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

    public void toggleFavoriteStatus(Contact contact, Favorite favorite) {
        contact.setFavorite(!contact.isFavorite());
        if (contact.isFavorite()) {
            favorite.setName(contact.getName());
            favorite.setLastname(contact.getLastname());
            favorite.setContactType(contact.getContactType());
            favorite.setValue(contact.getValue());
            favorite.setDateTime(contact.getDateTime());
            favorite.setUser(contact.getUser());
            favoriteRepository.save(favorite);
        }
        else favoriteRepository.delete(favorite);
        contactRepository.save(contact);
    }

    public List<Favorite> getFavoriteContacts(UserAccount currentUser) {
        return favoriteRepository.findByUser(currentUser);
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
