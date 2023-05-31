package com.example.directory.repository;

import com.example.directory.model.Contact;
import com.example.directory.model.Favorite;
import com.example.directory.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(UserAccount user);

    Favorite findByContact(Contact contact);
}
