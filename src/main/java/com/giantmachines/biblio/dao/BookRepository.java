package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
