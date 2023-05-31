package com.example.directory.dto;

import com.example.directory.model.ContactType;
import com.example.directory.validation.ValidUsername;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ContactDto {
    @ValidUsername
    private String name;

    @NotNull(message = "Surname is required.")
    @Size(max = 300, message = "The last name must be at least 300 characters long.")
    private String lastname;

    @NotNull(message = "Date is required.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTime;

    @NotNull(message = "ContactType is required.")
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @NotNull
    private String value;

    public ContactDto() {
    }

    public ContactDto(String name, String lastname, LocalDate dateTime, ContactType contactType, String value) {
        this.name = name;
        this.lastname = lastname;
        this.dateTime = dateTime;
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

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
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
