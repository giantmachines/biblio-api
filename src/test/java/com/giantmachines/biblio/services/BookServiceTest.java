package com.giantmachines.biblio.services;

import com.giantmachines.biblio.Application;
import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
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

    public void should_return_checkout_status_only_if_user_is_logged_in(){

    }
}
