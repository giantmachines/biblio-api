package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Query(value = "from Book b where b.status.value <> 4 and b.status.latest = 1")
    List<Book> getAllActiveBooks();
}
