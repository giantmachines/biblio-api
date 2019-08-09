package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;


@Service
@Transactional(readOnly = true)
public class BookService {

    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }


    public Iterable<Book> getAll(){
        return this.repository.findAll();
    }


    public Book getById(long id){
        return this.repository.findById(id).get();
    }

    @Transactional
    public void save(Book book) throws PersistenceException {
        this.repository.save(book);
    }

    public void delete(Book book) throws PersistenceException {
        this.repository.delete(book);
    }
}
