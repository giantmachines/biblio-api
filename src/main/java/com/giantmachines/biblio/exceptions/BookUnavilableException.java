package com.giantmachines.biblio.exceptions;

import com.giantmachines.biblio.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BookUnavilableException extends Exception{
    private Book book;

    public BookUnavilableException(Book book) {
        this.book = book;
    }

    @Override
    public String getMessage() {
        return String.format("The book %s is unavailable.", book.getTitle());
    }
}
