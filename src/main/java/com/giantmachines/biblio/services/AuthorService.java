package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Service
@Transactional(readOnly = true)
public class AuthorService {
    private AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Author getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    public Author save(Author author) throws PersistenceException {
        return this.repository.save(author);
    }
}
