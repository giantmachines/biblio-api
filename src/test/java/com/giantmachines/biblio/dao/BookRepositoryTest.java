package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.Status;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.*;

/**
 * Uses an in-memory database and tests the interaction with the
 * Book model as well.
 */
@Sql({"classpath:data.sql"})
public class BookRepositoryTest extends AbstractBaseJpaTest{

    @Autowired
    private BookRepository repository;



    @Test
    public void any_new_book_should_be_available_and_should_have_a_time_created_and_a_time_updated() {
        Author author = this.getEntityManager().find(Author.class, 1L);
        Book book = Book.builder().title("The Odyssey").author(author).build();
        this.getEntityManager().persist(book);
        assertNotNull(book.getStatus());
        assertTrue(book.getTimeUpdated() != 0);
    }


    @Test
    public void all_books_should_have_a_status_and_a_time_created_and_a_time_updated() {
        Iterable<Book> books = this.repository.findAll();
        for (Book book : books){
            assertNotNull(book.getStatus());
        }
    }


    @Test
    public void updates_should_set_the_time_updated() {
        Book book = this.repository.findById(2L).get();
        long timeUpdated = book.getTimeUpdated();
        book = book.toBuilder().status(Status.UNAVAILABLE).build();
        book = this.getEntityManager().merge(book);
        this.getEntityManager().flush();
        assertNotEquals(book.getTimeUpdated(), timeUpdated);
    }
}
