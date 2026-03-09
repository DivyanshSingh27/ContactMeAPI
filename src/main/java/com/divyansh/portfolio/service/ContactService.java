package com.divyansh.portfolio.service;

import com.divyansh.portfolio.dto.ContactRequest;
import com.divyansh.portfolio.dto.ContactResponse;
import com.divyansh.portfolio.model.Contact;
import com.divyansh.portfolio.repository.ContactRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private final ContactRepository repository;
    private final EmailService emailService;

    public ContactService(ContactRepository repository,
                          EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Transactional
    public ContactResponse saveMessage(ContactRequest request) {

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setMessage(request.getMessage());

        Contact saved = repository.save(contact);

        // Async email call (non-blocking)
        emailService.sendContactEmail(saved);

        return new ContactResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getMessage()
        );
    }
}