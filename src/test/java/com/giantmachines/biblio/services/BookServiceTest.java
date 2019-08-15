package com.giantmachines.biblio.services;

import com.giantmachines.biblio.Application;
import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        Book newBook = new Book("The Odyssey", author, null);
        newBook = service.save(newBook);

        List<Book> books = service.getAll();
        assertEquals(6, books.size());
        assertNotNull(newBook.getStatus());
    }

    @Test
    public void should_successfully_save_a_new_book_by_a_new_author(){
        Book newBook = new Book("On Tyranny", new Author("Timothy", "Snyder"),  null);
        service.save(newBook);

        List<Book> books = service.getAll();
        assertEquals(6, books.size());
    }

    @Test
    public void should_successfully_unregister_a_specified_book(){
        Book book = service.getById(3L);
        book = service.unregister(book);
        assertEquals("DEACTIVATED", book.getStatus().getValue().toString());

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
    public void should_save_a_new_review(){
        String comment = "A hated it.";
        User user = this.userRepository.findById(1L).get();
        Review review = new Review(user, 2, comment);
        Book book = service.getById(3L);
        book = service.addReview(book, review);
        review = book.getReviews().get(0);
        assertNotNull(review.getReviewer());
        assertNotEquals(0, review.getTimeCreated());
        assertNotEquals(0, review.getTimeUpdated());
        assertEquals(comment, review.getComments());
    }


    @Test
    @Sql({"classpath:tests.sql"})
    public void should_delete_an_existing_review(){
        assertEquals(1, service.getById(3L).getReviews().size());  // A control
        Review review = reviewRepository.findById(3L).get();
        service.deleteReview(3L, review.getId());
        assertEquals(0, service.getById(3L).getReviews().size());
    }

}
