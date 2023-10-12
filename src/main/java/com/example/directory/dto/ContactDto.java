package com.example.directory.dto;

import com.example.directory.model.ContactType;
import com.example.directory.validation.ValidUsername;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactDto {
    @ValidUsername
    @Size(min = 3, max = 20, message = "The name must be between 3 and 18 characters long.")
    @NotNull
    private String name;

    @ValidUsername
    @NotNull(message = "Lastname is required.")
    @Size(min = 3, max = 20, message = "The last name must be between 3 and 18 characters long.")
    private String lastname;

    @NotNull(message = "Country is required.")
    private String country;

    @NotNull(message = "ContactType is required.")
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @NotNull
    private String value;

    public ContactDto() {
    }

    public ContactDto(String name, String lastname, String country, ContactType contactType, String value) {
        this.name = name;
        this.lastname = lastname;
        this.country = country;
        this.contactType = contactType;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
