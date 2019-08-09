package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.AuthorRepository;
import com.giantmachines.biblio.dao.BookRepository;
import com.giantmachines.biblio.model.Author;
import com.giantmachines.biblio.model.Book;
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


    @Autowired
    public BookService(BookRepository repository, AuthorService authorService) {
        this.repository = repository;
        this.authorService = authorService;
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
    public void save(Book book) throws PersistenceException {
        Author author = book.getAuthor();
        if (authorService.getById(author.getId()) == null){
            this.authorService.save(author);
        }
        this.repository.save(book);
    }

    @Transactional
    public void delete(Book book) throws PersistenceException {
        this.repository.delete(book);
    }
}
