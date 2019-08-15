package com.giantmachines.biblio.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.security.CurrentUser;
import com.giantmachines.biblio.services.BookService;
import com.giantmachines.biblio.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/books")
public class BookController extends AbstractBaseController {

    private String path = "books";
    private BookService service;
    private ReviewService reviewService;
    private CurrentUser currentUser;


    @Autowired
    public BookController(BookService service, ReviewService reviewService, CurrentUser currentUser) {
        this.service = service;
        this.reviewService = reviewService;
        this.currentUser = currentUser;
    }

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

    @RequestMapping(value = "/{id}/reviews", method = RequestMethod.GET)
    public ResponseEntity getReviewsByBookId(@PathVariable("id") long id){
        Book book = this.service.getById(id);
        return this.buildOkResponse(book.getReviews());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity search() {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Book book) throws Exception {
        this.service.save(book);
        return this.buildCreatedResponse(book.getId());
    }

    /**
     * Performs a "soft delete."  Does not remove the book from the database, for
     * historical reasons, but sets the book to "inactive."
     *
     * @param book          the book to remove.
     * @return              a ResponseEntity
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Book book) {
        this.service.unregister(book);
        return this.buildCreatedResponse(book.getId());
    }


    @RequestMapping(value = "{bookId}/review", method = RequestMethod.POST)
    public ResponseEntity saveReview(@PathVariable("bookId") long bookId, @RequestBody Review review){
        Book book = this.service.getById(bookId);
        book = this.service.addReview(book, review);
        return this.buildOkResponse(new BookDetailsDto(book));
    }


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


    @RequestMapping(value = "{bookId}/review/{reviewId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteReview(@PathVariable("bookId") long bookId,
                                       @PathVariable("reviewId") long reviewId){
        Book book = this.service.deleteReview(bookId, reviewId);
        return this.buildOkResponse(new BookDetailsDto(book));
    }


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
            String userName = BookController.this.currentUser.get();
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.image = book.getImage();
            if (userName != null){
                this.highlight = false;
                this.status = book.getStatus().getValue().toString();
                if (book.getStatus().getUser() != null
                        && userName.equals(book.getStatus().getUser().getEmail())) {
                    this.highlight = true;
                }
            }
            double rating = book.getReviews().stream().mapToDouble(Review::getValue).average().orElse(-1.0);
            this.averageRating = rating > 0 ? rating : null;
        }

        public long getId() {
            return id;
        }

        public Author getAuthor() {
            return author;
        }

        public String getImage() {
            return image;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public String getStatus() {
            return status;
        }

        public String getTitle() {
            return title;
        }

        public Boolean getHighlight() {
            return highlight;
        }
    }

    private class BookDetailsDto extends BookDto{
        private List<Review> reviews;
        BookDetailsDto(Book book) {
            super(book);
            this.reviews = book.getReviews();
        }

        public List<Review> getReviews() {
            return reviews;
        }
    }
}




