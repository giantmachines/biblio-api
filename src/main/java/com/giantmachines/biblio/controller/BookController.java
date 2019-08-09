package com.giantmachines.biblio.controller;

import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll() {
        return ResponseUtility.buildOkResponse(this.service.getAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable long id) {
        return ResponseUtility.buildOkResponse(this.service.getById(id));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity search(){
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Book book) throws Exception{
        this.service.save(book);
        return ResponseUtility.buildCreatedResponse(book.getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Book book) throws Exception{
        this.service.delete(book);
        return ResponseUtility.buildCreatedResponse(book.getId());
    }
}