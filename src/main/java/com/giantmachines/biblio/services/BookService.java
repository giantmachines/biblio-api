package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides services for retrieving and updating books
 */
@Service
@Transactional(readOnly = true)
public class BookService {

    private BookRepository repository;
    private AuthorService authorService;
    private StatusService statusService;
    private ReviewService reviewService;


    @Autowired
    public BookService(BookRepository repository,
                       AuthorService authorService,
                       StatusService statusService,
                       ReviewService reviewService) {
        this.repository = repository;
        this.authorService = authorService;
        this.statusService = statusService;
        this.reviewService = reviewService;
    }



    public List<Book> getAll(){
        List<Book> result = new ArrayList<>();
        this.repository.findAll().forEach(result::add);
        return result;
    }

    public List<Book> getActiveOnly(){
        return this.repository.getAllActiveBooks();
    }


    public Book getById(long id){
        return this.repository.findById(id).get();
    }


    @Transactional
    public Book save(Book book) throws PersistenceException {
        Author author = book.getAuthor();
        BookStatus status = book.getStatus();
        BookStatus currentStatus = statusService.getById(status.getId());

        // Adding a new author if the book's author does not exist.
        if (authorService.getById(author.getId()) == null){
            this.authorService.save(author);
        }

        // We want to ensure that every book has a status, and to update the status id needed.
        if (currentStatus == null || !currentStatus.getValue().equals(status.getValue())){
            this.statusService.save(status);
            Book.BookBuilder builder = new Book.BookBuilder(book);
            builder.setStatus(status);
            if (currentStatus != null){
                this.statusService.save(new BookStatus(currentStatus));
            }
            book = new Book(builder);
        }

        return this.repository.save(book);
    }


    @Transactional
    public Book unregister(Book book) throws PersistenceException {
        BookStatus status = new BookStatus(Status.DEACTIVATED, book);
        statusService.save(new BookStatus(book.getStatus()));
        statusService.save(status);
        Book.BookBuilder builder = new Book.BookBuilder(book);
        builder.setStatus(status);
        return this.repository.save(new Book(builder));
    }

    @Transactional
    public Book deleteReview(long bookId, long id){
        this.reviewService.delete(id);
        return this.getById(bookId);
    }
}
