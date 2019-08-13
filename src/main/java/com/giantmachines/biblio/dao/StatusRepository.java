package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.BookStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusRepository extends CrudRepository<BookStatus, Long> {
    @Query(value = "from BookStatus s where s.book.id = :bookId")
    List<BookStatus> getByBookId(@Param("bookId") long bookId);
}
