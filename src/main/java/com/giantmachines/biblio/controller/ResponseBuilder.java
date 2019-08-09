package com.giantmachines.biblio.controller;

import org.springframework.http.ResponseEntity;

import java.net.URI;

class ResponseBuilder {
    private String path;

    ResponseBuilder(String path) {
        this.path = path;
    }

    ResponseEntity buildOkResponse(Object entity) {
        return ResponseEntity.ok().body(entity);
    }

    ResponseEntity buildCreatedResponse(Long id) {
        String url = String.format("http:localhost:8080/biblio/%s/", this.path);
        URI location = URI.create(url + id);
        return ResponseEntity.created(location).build();
    }
}

