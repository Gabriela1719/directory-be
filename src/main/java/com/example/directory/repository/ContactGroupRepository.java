package com.example.directory.repository;

import com.example.directory.model.ContactGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {
    ContactGroup findByNameAndLastname(String name, String lastname);
}
