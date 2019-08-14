package com.giantmachines.biblio.controller;

import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.security.CurrentUser;
import com.giantmachines.biblio.services.BookService;
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
    private CurrentUser currentUser;

    @Autowired
    public BookController(BookService service, CurrentUser currentUser) {
        this.service = service;
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
        return this.buildOkResponse(this.service.getById(id));
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


    private class BookDto {
        private long id;
        private String title;
        private Author author;
        private String image;
        /**
         * Average rating
         */
        private double rating;
        /**
         * Book status value
         */
        private String status;
        private boolean userItem = false;  //TODO

        BookDto(final Book book) {
            String userName = BookController.this.currentUser.get();
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.image = book.getImage();
            if (!userName.equals("anonymous")){
                this.status = book.getStatus().getValue().toString();
                if (book.getStatus().getUser() != null
                        && userName.equals(book.getStatus().getUser().getEmail())) {
                    this.userItem = true;
                }
            }
            this.rating = book.getReviews().stream().mapToDouble(Review::getValue).average().orElse(-1.0);
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

        public double getRating() {
            return rating;
        }

        public String getStatus() {
            return status;
        }

        public String getTitle() {
            return title;
        }

        public boolean isUserItem() {
            return userItem;
        }
    }
}




