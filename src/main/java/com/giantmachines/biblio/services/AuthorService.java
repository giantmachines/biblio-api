package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

/**
 * Provides services for retrieving and updating authors
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {
    private final AuthorRepository repository;

    public Author getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    public Author save(Author author) throws PersistenceException {
        return this.repository.save(author);
    }
}
