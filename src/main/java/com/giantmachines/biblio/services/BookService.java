package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.exceptions.BookUnavailableException;
import com.giantmachines.biblio.model.*;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final StatusService statusService;
    private final UserService userService;


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
            book = book.toBuilder().status(status).build();
            if (currentStatus != null){
                this.statusService.save(currentStatus.toBuilder().build());
            }
        }

        return this.repository.save(book);
    }


    @Transactional
    public Book addReview(Book book, Review review){
        book = this.getById(book.getId());
        List<Review> reviews = book.getReviews();
        reviews.add(review);
        Book result = book.toBuilder()
                .reviews(reviews)
                .build();
        return this.repository.save(result);
    }


    @Transactional
    public Book unregister(Book book) throws PersistenceException {
        BookStatus newStatus = BookStatus.builder()
                .value(Status.DEACTIVATED)
                .book(book)
                .build();
        BookStatus prevStatus = book.getStatus().toBuilder().latest(false).build();
        statusService.save(prevStatus);
        statusService.save(newStatus);
        Book result = book.toBuilder()
                .status(newStatus)
                .build();
        return this.repository.save(result);
    }

    @Transactional
    public Book deleteReview(long bookId, long id){
        Book book = this.getById(bookId);
        List<Review> reviews = book
                .getReviews()
                .stream()
                .filter(review -> review.getId() != id)
                .collect(Collectors.toList());
        Book result = book.toBuilder().reviews(reviews).build();
        return this.repository.save(result);
    }

    @Transactional
    public Book checkout(Book book, long userId) throws BookUnavailableException, PersistenceException{
        Book current = this.getById(book.getId());
        if (!current.getStatus().getValue().equals(Status.AVAILABLE)){
            throw new BookUnavailableException(book);
        }

        User user =  this.userService.getById(userId);
        BookStatus newStatus = BookStatus.builder()
                .value(Status.UNAVAILABLE)
                .book(current)
                .user(user)
                .build();
        Book result = current.toBuilder()
                .status(newStatus)
                .build();
        return this.save(result);
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

        User user =  this.userService.getById(userId);
        BookStatus newStatus = BookStatus.builder()
                .value(Status.AVAILABLE)
                .book(current)
                .user(user)
                .build();
        Book result = current.toBuilder()
                .status(newStatus)
                .build();
        return this.save(result);
    }


    public boolean hasReviews(long id){
        return this.getById(id).getReviews().size() > 0;
    }
}
