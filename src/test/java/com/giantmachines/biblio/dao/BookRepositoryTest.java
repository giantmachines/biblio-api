package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.Application;
import com.giantmachines.biblio.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void books_should_always_have_statuses(){
        Iterable<Book> books = this.repository.findAll();
        for (Book book : books){
            assertNotNull(book.getStatus());
        }
    }
}
