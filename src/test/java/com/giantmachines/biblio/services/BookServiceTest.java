package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.exceptions.BookUnavailableException;
import com.giantmachines.biblio.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;



public class BookServiceTest extends AbstractBaseServiceTest{

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
    @DirtiesContext
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
    @DirtiesContext
    public void should_successfully_save_a_new_book_by_a_new_author(){
        Author author = Author.builder()
                .firstName("Timothy")
                .lastName("Snyder")
                .build();
        Book newBook = Book.builder()
                .title("On Tyranny")
                .author(author)
                .build();
        service.save(newBook);

        List<Book> books = service.getAll();
        assertEquals(6, books.size());
    }


    @Test
    @DirtiesContext
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
        Book book = service.getById(3L);
        Review review = Review.builder()
                .reviewer(user)
                .value(2)
                .book(book)
                .comments(comment)
                .build();
        review = service.saveReview(review);
        assertNotNull(review.getReviewer());
        assertNotEquals(0, review.getTimeCreated());
        assertNotEquals(0, review.getTimeUpdated());
        assertEquals(comment, review.getComments());
    }


    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_delete_an_existing_review(){
        Book book = service.getById(3L);
        assertFalse(service.getReviews(book).isEmpty());  // A control
        Review review = reviewRepository.findById(2L).get();
        service.deleteReview(review.getId());
        assertTrue(service.getReviews(book).isEmpty());
    }


    @Test
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_checkout_available_books() throws Exception{
        try {
            Book book = service.getById(4L);
            book = service.checkout(book);
            assertEquals(Status.UNAVAILABLE, book.getStatus());
        } catch(BookUnavailableException e){
            fail("A BookUnavailableException should not have been thrown.");
        }
    }


    @Test(expected = BookUnavailableException.class)
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_not_allow_checkout_unless_the_book_is_available() throws Exception {
        Book book = service.getById(1L);
        service.checkout(book);
    }


    @Test
    @Sql({"classpath:tests.sql"})
    @WithMockUser("paford@gmail.com")
    @DirtiesContext
    public void should_checkin_books_that_are_checked_out(){
        try {
            Book book = service.getById(2L);
            book = service.checkin(book);
            assertEquals(Status.AVAILABLE, book.getStatus());
        } catch (Exception e){
            fail("An exception should not be thrown.");
        }
    }


    @Test(expected = IllegalAccessException.class)
    @Sql({"classpath:tests.sql"})
    @DirtiesContext
    public void should_not_checkin_books_that_are_checked_out_by_others() throws Exception {
        Book book = service.getById(1L);
        service.checkin(book);
    }
}
