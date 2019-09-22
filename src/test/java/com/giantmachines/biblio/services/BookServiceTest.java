package com.giantmachines.biblio.services;

import com.giantmachines.biblio.Application;
import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.exceptions.BookUnavailableException;
import com.giantmachines.biblio.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Sql({"classpath:reset.sql"})
public class BookServiceTest {

    @Autowired
    BookService service;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    public void should_return_the_correct_book_for_the_requested_id(){
        Book book = service.getById(1);
        assertEquals("The Iliad", book.getTitle());

        book = service.getById(3);
        assertEquals("Refactoring", book.getTitle());
    }

    @Test
    public void should_return_all_books(){
        List<Book> books = service.getAll();
        assertEquals(5, books.size());

        List<String> titles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        assertTrue(titles.contains("Refactoring"));
        assertTrue(titles.contains("The Iliad"));
        assertTrue(titles.contains("Design Patterns"));
        assertFalse(titles.contains("ABCD"));
        assertTrue(titles.contains("1994"));
    }

    @Test
    public void should_return_all_active_books(){
        List<Book> books = service.getActiveOnly();
        assertEquals(4, books.size());

        List<String> titles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        assertTrue(titles.contains("Refactoring"));
        assertTrue(titles.contains("The Iliad"));
        assertTrue(titles.contains("Design Patterns"));
        assertFalse(titles.contains("ABCD"));
    }


    @Test
    public void should_successfully_save_a_new_book_by_an_existing_author(){
        Author author = authorRepository.findById(1L).get();
        Book newBook = Book.builder()
                .title("The Odyssey")
                .author(author)
                .build();
        newBook = service.save(newBook);

        List<Book> books = service.getAll();
        assertEquals(6, books.size());
        assertNotNull(newBook.getStatus());
    }

    @Test
    public void should_successfully_save_a_new_book_by_a_new_author(){
        Book newBook = Book.builder()
                .title("On Tyranny")
                .author(new Author("Timothy", "Snyder"))
                .build();
        service.save(newBook);

        List<Book> books = service.getAll();
        assertEquals(6, books.size());
    }

    @Test
    public void should_successfully_unregister_a_specified_book(){
        Book book = service.getById(3L);
        book = service.unregister(book);
        assertEquals("DEACTIVATED", book.getStatus().toString());

        List<Book> books = service.getActiveOnly();
        assertEquals(3, books.size());

        List<String> titles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        assertFalse(titles.contains("Refactoring"));
    }

    @Test
    public void should_return_checkout_status(){
        List<Book> books = this.service.getAll();
        for (Book book : books){
            assertNotNull(book.getStatus());
        }
    }

    @Test
    @DirtiesContext
    public void should_save_a_new_review(){
        String comment = "A hated it.";
        User user = this.userRepository.findById(1L).get();
        Review review = Review.builder()
                .reviewer(user)
                .value(2)
                .comments(comment)
                .build();
        Book book = service.getById(3L);
        book = service.addReview(book, review);
        review = book.getReviews().get(0);
        assertNotNull(review.getReviewer());
        assertNotEquals(0, (long) review.getTimeCreated());
        assertNotEquals(0, (long) review.getTimeUpdated());
        assertEquals(comment, review.getComments());
    }


    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_delete_an_existing_review(){
        assertTrue(service.hasReviews(3L));  // A control
        Review review = reviewRepository.findById(2L).get();
        service.deleteReview(3L, review.getId());
        assertFalse(service.hasReviews(3L));
    }

    @Test
    @DirtiesContext
    public void should_checkout_available_boos() throws Exception{
        Book book = service.getById(3L);
        book = service.checkout(book, 1L);
        assertEquals(Status.UNAVAILABLE, book.getStatus());
    }

    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_not_allow_checkout_unless_the_book_is_available(){
        Book book = service.getById(1L);
        try {
            service.checkout(book, 2L);
            fail("We should not reach this point.");
        } catch (Exception e){
            assertTrue(e instanceof BookUnavailableException);
        }
    }

    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_checkin_books_that_are_checked_out(){
        try {
            Book book = service.getById(1L);
            book = service.checkin(book, 1L);
            assertEquals(Status.AVAILABLE, book.getStatus());
        } catch (Exception e){
            fail("An exception should not be thrown.");
        }
    }

    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_not_checkin_books_that_are_checked_out_by_others(){
        try {
            Book book = service.getById(1L);
            book = service.checkin(book, 2L);
            fail("We should not reach this point.");
        } catch (Exception e){
            assertTrue(e instanceof IllegalAccessException);
        }
    }
}
