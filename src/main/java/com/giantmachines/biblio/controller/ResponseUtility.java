package com.giantmachines.biblio.controller;

import org.springframework.http.ResponseEntity;

import java.net.URI;

class ResponseUtility {

    static ResponseEntity buildOkResponse(Object entity) {
        return ResponseEntity.ok().body(entity);
    }

    static ResponseEntity buildCreatedResponse(Long id) {
        URI location = URI.create("http:localhost:8080/users/" + id);
        return ResponseEntity.created(location).build();
    }
}
