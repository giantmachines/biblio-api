package com.giantmachines.biblio.controller;

import com.giantmachines.biblio.model.Book;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @RequestMapping(method = RequestMethod.GET)
    public List<Book> getAll() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Book getById() {
        return null;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Book> search(){
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public long save(@RequestBody Book book){
        return 0;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public long delete(){
        return 0;
    }
}
