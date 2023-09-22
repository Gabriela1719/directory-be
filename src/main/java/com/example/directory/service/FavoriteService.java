package com.example.directory.service;

import com.example.directory.model.Contact;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import com.example.directory.repository.ContactRepository;
import com.example.directory.repository.FavoriteRepository;
import com.example.directory.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private final ContactRepository contactRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserAccountRepository userAccountRepository;

    public FavoriteService(ContactRepository contactRepository, FavoriteRepository favoriteRepository, UserAccountRepository userAccountRepository) {
        this.contactRepository = contactRepository;
        this.favoriteRepository = favoriteRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email).stream().findFirst();
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void toggleFavoriteStatus(Long id, Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        Optional<Contact> optionalContact = contactRepository.findById(id);
        Contact contact = optionalContact.orElseThrow(() -> new RuntimeException("Contact not found"));

        if (!contact.getUser().equals(currentUser)) {
            throw new RuntimeException("Contact not found");
        }

        boolean isFavorite = contact.isFavorite();
        contact.setFavorite(!isFavorite);

        if (isFavorite) {
            List<Favorite> favorites = contact.getFavorites();
            if (favorites != null && !favorites.isEmpty()) {
                favoriteRepository.deleteAll(favorites);
                favorites.clear();
            }
        } else {
            Favorite existingFavorite = favoriteRepository.findByContact(contact);
            if (existingFavorite == null) {
                Favorite favorite = new Favorite();
                favorite.setId(contact.getId());
                favorite.setName(contact.getName());
                favorite.setLastname(contact.getLastname());
                favorite.setContactType(contact.getContactType());
                favorite.setValue(contact.getValue());
                favorite.setCountry(contact.getCountry());
                favorite.setUser(contact.getUser());
                favorite.setContact(contact);

                favoriteRepository.save(favorite);
            }
        }

        contactRepository.save(contact);
    }

    public List<Favorite> getFavoriteContacts(Authentication authentication) {
        UserAccount currentUser = getCurrentUser(authentication);
        return favoriteRepository.findByUser(currentUser);
    }

}
