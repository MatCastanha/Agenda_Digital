package com.example.agenda.dto;

import com.example.agenda.model.Contact;
import java.time.LocalDateTime;

public class ContactResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String notes;
    private LocalDateTime createdAt;

    public static ContactResponse fromEntity(Contact c) {
        ContactResponse res = new ContactResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setEmail(c.getEmail());
        res.setPhone(c.getPhone());
        res.setNotes(c.getNotes());
        res.setCreatedAt(c.getCreatedAt());
        return res;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
