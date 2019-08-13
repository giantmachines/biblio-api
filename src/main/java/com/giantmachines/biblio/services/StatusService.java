package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.StatusRepository;
import com.giantmachines.biblio.model.BookStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides services for retrieving and updating book statuses
 */
@Service
@Transactional(readOnly = true)
public class StatusService {
    private StatusRepository repository;

    public StatusService(StatusRepository repository) {
        this.repository = repository;
    }

    public BookStatus save(BookStatus status){
        this.repository.save(status);
        return status;
    }

    public BookStatus getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    public List<BookStatus> getHistory(long bookId){
        return this.repository.getByBookId(bookId);
    }
}
