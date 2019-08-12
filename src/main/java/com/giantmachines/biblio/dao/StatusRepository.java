package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.BookStatus;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<BookStatus, Long> {
}
