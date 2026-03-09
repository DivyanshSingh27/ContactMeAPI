package com.divyansh.portfolio.controller;

import com.divyansh.portfolio.dto.ContactRequest;
import com.divyansh.portfolio.dto.ContactResponse;
import com.divyansh.portfolio.response.ApiResponse;
import com.divyansh.portfolio.service.ContactService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactResponse>> submitMessage(
            @Valid @RequestBody ContactRequest request
    ) {

        ContactResponse savedResponse = service.saveMessage(request);

        return ResponseEntity.ok(
                ApiResponse.success("Message sent successfully", savedResponse)
        );
    }
}