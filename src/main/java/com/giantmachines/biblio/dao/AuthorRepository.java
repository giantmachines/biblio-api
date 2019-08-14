package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
