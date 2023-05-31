package com.example.directory.service;

import com.example.directory.dto.ContactDto;
import com.example.directory.model.Contact;
import com.example.directory.model.ContactGroup;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactGroupRepository;
import com.example.directory.repository.ContactRepository;
import com.example.directory.repository.FavoriteRepository;
import com.example.directory.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final FavoriteRepository favoriteRepository;
    private final ContactGroupRepository contactGroupRepository;

    private final UserAccountRepository userAccountRepository;

    public ContactService(ContactRepository contactRepository, FavoriteRepository favoriteRepository, ContactGroupRepository contactGroupRepository, UserAccountRepository userAccountRepository) {
        this.contactRepository = contactRepository;
        this.favoriteRepository = favoriteRepository;
        this.contactGroupRepository = contactGroupRepository;
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

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Contact createContact(Contact contact, UserAccount currentUser) {
        ContactGroup group = contactGroupRepository.findByNameAndLastname(contact.getName(), contact.getLastname());

        if (group == null) {
            group = new ContactGroup();
            group.setName(contact.getName());
            group.setLastname(contact.getLastname());
            group = contactGroupRepository.save(group);
        }
        contact.setGroup(group);
        contact.setUser(currentUser);
        return contactRepository.save(contact);
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
        favoriteRepository.deleteById(id);
        contactRepository.deleteById(id);
    }

    //FAVORITE

    public void toggleFavoriteStatus(Contact contact) {
        contact.setFavorite(!contact.isFavorite());

        if (contact.isFavorite()) {
            Favorite favorite = new Favorite();
            favorite.setName(contact.getName());
            favorite.setLastname(contact.getLastname());
            favorite.setContactType(contact.getContactType());
            favorite.setValue(contact.getValue());
            favorite.setDateTime(contact.getDateTime());
            favorite.setUser(contact.getUser());
            favorite.setContact(contact);
            favoriteRepository.save(favorite);
        } else {
            List<Favorite> favorites = contact.getFavorites();
            if (favorites != null && !favorites.isEmpty()) {
                favoriteRepository.deleteAll(favorites);
                favorites.clear();
            }
        }
        contactRepository.save(contact);
    }

    public List<Favorite> getFavoriteContacts(UserAccount currentUser) {
        return favoriteRepository.findByUser(currentUser);
    }

}
