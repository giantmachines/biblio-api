package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class BookService {

    private BookRepository repository;
    private AuthorService authorService;
    private StatusService statusService;


    @Autowired
    public BookService(BookRepository repository, AuthorService authorService, StatusService statusService) {
        this.repository = repository;
        this.authorService = authorService;
        this.statusService = statusService;
    }


    public List<Book> getAll(){
        List<Book> result = new ArrayList<>();
        this.repository.findAll().forEach(result::add);
        return result;
    }


    public Book getById(long id){
        return this.repository.findById(id).get();
    }


    @Transactional
    public Book save(Book book) throws PersistenceException {
        Author author = book.getAuthor();
        BookStatus status = book.getStatus();
        BookStatus currentStatus = statusService.getById(status.getId());

        // Adding a new author if the book's author does not exist.
        if (authorService.getById(author.getId()) == null){
            this.authorService.save(author);
        }

        // We want to ensure that every book has a status, and to update the status id needed.
        if (currentStatus == null || !currentStatus.getValue().equals(status.getValue())){
            this.statusService.save(status);
            Book.BookBuilder builder = new Book.BookBuilder(book);
            builder.setStatus(status);
            book = new Book(builder);
        }

        return this.repository.save(book);
    }


    @Transactional
    public void delete(Book book) throws PersistenceException {
        this.repository.delete(book);
    }
}
