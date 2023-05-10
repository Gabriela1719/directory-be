package com.example.directory.repository;

import com.example.directory.model.Contact;
import com.example.directory.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUser(UserAccount currentUser);

    List<Contact> findByUserAndFavorite(UserAccount user, boolean b);
}
