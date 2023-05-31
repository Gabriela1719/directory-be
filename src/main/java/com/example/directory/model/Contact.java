package com.example.directory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    private LocalDate dateTime;

    private ContactType contactType;

    private String value;

    private boolean favorite;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private ContactGroup group;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;

    public Contact() {
    }

    public Contact(String name, String lastname, LocalDate dateTime, ContactType contactType, String value, boolean favorite, UserAccount user, ContactGroup group, List<Favorite> favorites) {
        this.name = name;
        this.lastname = lastname;
        this.dateTime = dateTime;
        this.contactType = contactType;
        this.value = value;
        this.favorite = favorite;
        this.user = user;
        this.group = group;
        this.favorites = favorites;
    }


    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ContactGroup getGroup() {
        return group;
    }

    public void setGroup(ContactGroup group) {
        this.group = group;
    }
}
