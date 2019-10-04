package com.giantmachines.biblio.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.giantmachines.biblio.model.*;
import com.giantmachines.biblio.services.BookService;
import com.giantmachines.biblio.services.ReviewService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController extends AbstractBaseController {

    private final String path = "books";
    private final BookService service;
    private final ReviewService reviewService;
    private final AuditorAware<User> auditService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll() {
        List<BookDto> summaries = this.service.getActiveOnly().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
        return this.buildOkResponse(summaries);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable long id) {
        return this.buildOkResponse(new BookDetailsDto(this.service.getById(id)));
    }

    @RequestMapping(value = "/{bookId}/reviews", method = RequestMethod.GET)
    public ResponseEntity getReviewsByBookId(@PathVariable("bookId") long id){
        Book book = this.service.getById(id);
        return this.buildOkResponse(book.getReviews());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Book book) {
        this.service.save(book);
        return this.buildCreatedResponse(book.getId());
    }

    /**
     * Performs a "soft delete."  Does not remove the book from the database, for
     * historical reasons, but sets the book to "inactive."
     *
     * @param bookId        the id of book to remove.
     * @return              a ResponseEntity
     */
    @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("bookId") long bookId) {
        Book book = this.service.getById(bookId);
        this.service.unregister(book);
        return this.buildCreatedResponse(book.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "{bookId}/review", method = RequestMethod.POST)
    public ResponseEntity saveReview(@PathVariable("bookId") long bookId, @RequestBody Review review){
        Book book = this.service.getById(bookId);
        book = this.service.addReview(book, review);
        return this.buildOkResponse(new BookDetailsDto(book));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "{bookId}/review", method = RequestMethod.PUT)
    public ResponseEntity updateReview(@PathVariable("bookId") long bookId, @RequestBody Review review) throws Exception{
        Review current = this.reviewService.getById(review.getId());
        if (current == null){
            throw new Exception("The resource was not found.");
        }
        this.reviewService.update(review);
        Book book = this.service.getById(bookId);
        return this.buildOkResponse(new BookDetailsDto(book));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "{bookId}/review/{reviewId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteReview(@PathVariable("bookId") long bookId,
                                       @PathVariable("reviewId") long reviewId){
        Book book = this.service.deleteReview(bookId, reviewId);
        return this.buildOkResponse(new BookDetailsDto(book));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{bookId}/checkout", method = RequestMethod.PUT)
    public ResponseEntity checkout(@PathVariable("bookId") long bookId) throws Exception{
        Book book = this.service.getById(bookId);
        book = this.service.checkout(book);
        return this.buildOkResponse(new BookDetailsDto(book));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{bookId}/checkin", method = RequestMethod.PUT)
    public ResponseEntity checkin(@PathVariable("bookId") long bookId) {
        Book book = this.service.getById(bookId);
        try {
            book = this.service.checkin(book);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return this.buildOkResponse(new BookDetailsDto(book));
    }


    @Getter
    private class BookDto {
        private long id;
        private String title;
        private Author author;
        private String image;
        /** Average rating */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double averageRating;
        /** Book status value */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String status;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean highlight = null;

        BookDto(final Book book) {
            String userName = BookController.this.auditService
                    .getCurrentAuditor()
                    .get()
                    .getEmail();
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.image = book.getImage();
            if (userName != null){
                this.highlight = false;
                this.status = book.getStatus().toString();
                if (book.getStatus().equals(Status.UNAVAILABLE)
                        && book.getLastModifiedBy() != null
                        && userName.equals(book.getLastModifiedBy().getEmail())) {
                    this.highlight = true;
                }
            }
            double rating = book.getReviews().stream().mapToDouble(Review::getValue).average().orElse(-1.0);
            this.averageRating = rating > 0 ? rating : null;
        }
    }

    @Getter
    private class BookDetailsDto extends BookDto{
        private List<Review> reviews;

        BookDetailsDto(Book book) {
            super(book);
            this.reviews = book.getReviews();
        }
    }
}




