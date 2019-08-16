package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.exceptions.BookUnavailableException;
import com.giantmachines.biblio.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides services for retrieving and updating books
 */
@Service
@Transactional(readOnly = true)
public class BookService {

    private BookRepository repository;
    private AuthorService authorService;
    private StatusService statusService;
    private UserService userService;


    @Autowired
    public BookService(BookRepository repository,
                       AuthorService authorService,
                       StatusService statusService,
                       UserService userService) {
        this.repository = repository;
        this.authorService = authorService;
        this.statusService = statusService;
        this.userService = userService;
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
    public Book addReview(Book book, Review review){
        List<Review> reviews = book.getReviews();
        reviews.add(review);
        Book.BookBuilder builder = new Book.BookBuilder(book);
        builder.setReviews(reviews);
        return this.repository.save(new Book(builder));
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
        Book book = this.getById(bookId);
        List<Review> reviews = book
                .getReviews()
                .stream()
                .filter(review -> review.getId() != id)
                .collect(Collectors.toList());
        Book.BookBuilder builder = new Book.BookBuilder(book).setReviews(reviews);
        return this.repository.save(new Book(builder));
    }

    @Transactional
    public Book checkout(Book book, long userId) throws BookUnavailableException, PersistenceException{
        Book current = this.getById(book.getId());
        if (!current.getStatus().getValue().equals(Status.AVAILABLE)){
            throw new BookUnavailableException(book);
        }

        Book.BookBuilder builder = new Book.BookBuilder(current);
        User user =  this.userService.getById(userId);
        builder.setStatus(new BookStatus(Status.UNAVAILABLE, current, user));
        return this.save(new Book(builder));
    }


    @Transactional
    public Book checkin(Book book, long userId) throws IllegalStateException, IllegalAccessException{
        Book current = this.getById(book.getId());
        if (current.getStatus().getValue().equals(Status.AVAILABLE)){
            throw new IllegalStateException("Attempt to checkin a book that was not checked out.");
        }
        if (current.getStatus().getUser().getId() != userId){
            throw new IllegalAccessException("Attempt to checkin a book that was checked out by another user.");
        }

        Book.BookBuilder builder = new Book.BookBuilder(current);
        User user =  this.userService.getById(userId);
        builder.setStatus(new BookStatus(Status.AVAILABLE, current, user));
        return this.save(new Book(builder));
    }
}
