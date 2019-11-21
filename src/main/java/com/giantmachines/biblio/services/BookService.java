package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.exceptions.BookUnavailableException;
import com.giantmachines.biblio.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides services for retrieving and updating books
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final AuditorAware<User> auditService;
    private final ReviewRepository reviewRepository;


    public List<Book> getAll(){
        List<Book> result = new ArrayList<>();
        this.repository.findAll().forEach(result::add);
        return result;
    }

    public List<Book> getActiveOnly(){
        return this.repository.getAllActiveBooks();
    }


    public Book getById(long id){
        return this.repository.findById(id).orElseThrow();
    }


    @Transactional
    public Book save(Book book) throws PersistenceException {
        Author author = book.getAuthor();

        // Adding a new author if the book's author does not exist.
        if (authorService.getById(author.getId()) == null){
            this.authorService.save(author);
        }

        return this.repository.save(book);
    }


    public boolean highlight(Book book){
        String userName = this.auditService
                .getCurrentAuditor()
                .orElse(new User())
                .getEmail();
        boolean highlight = false;
        if (userName != null){
            if (book.getStatus().equals(Status.UNAVAILABLE)
                    && book.getLastModifiedBy() != null
                    && userName.equals(book.getLastModifiedBy().getEmail())) {
                highlight = true;
            }
        }
        return highlight;
    }


    public Double getAverageRating(Book book){
        double rating = this.getReviews(book)
                .stream()
                .mapToDouble(Review::getValue).average().orElse(-1.0);
        return rating > 0 ? rating : null;
    }


    public List<Review> getReviews(Book book){
        return this.reviewRepository.getByBook(book);
    }


    @Transactional
    public Review saveReview(Review review){
        return this.reviewRepository.save(review);
    }


    @Transactional
    public Review updateReview(Review review) {
        Review current = this.reviewRepository.findById(review.getId()).orElseThrow();
        Review result = current.toBuilder()
                .value(review.getValue())
                .comments(review.getComments())
                .build();
        return this.reviewRepository.save(result);
    }


    @Transactional
    public Book unregister(Book book) throws PersistenceException {
        Book result = book.toBuilder()
                .status(Status.DEACTIVATED)
                .build();
        return this.repository.save(result);
    }

    @Transactional
    public void deleteReview(long reviewId) {
        Review review = this.reviewRepository.findById(reviewId).orElse(null);
        this.reviewRepository.delete(review);
    }

    @Transactional
    public Book checkout(Book book) throws BookUnavailableException, PersistenceException{
        Book current = this.getById(book.getId());
        if (!current.getStatus().equals(Status.AVAILABLE)){
            throw new BookUnavailableException(book);
        }

        Book result = current.toBuilder()
                .status(Status.UNAVAILABLE)
                .build();
        return this.save(result);
    }


    @Transactional
    public Book checkin(Book book) throws IllegalStateException, IllegalAccessException{
        Book current = this.getById(book.getId());
        if (current.getStatus().equals(Status.AVAILABLE)){
            throw new IllegalStateException("Attempt to checkin a book that was not checked out.");
        }
        if (current.getLastModifiedBy().getId() != auditService.getCurrentAuditor().get().getId()){
            throw new IllegalAccessException("Attempt to checkin a book that was checked out by another user.");
        }

        Book result = current.toBuilder()
                .status(Status.AVAILABLE)
                .build();
        return this.save(result);
    }
}
