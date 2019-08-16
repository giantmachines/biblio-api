package com.giantmachines.biblio.exceptions;

import com.giantmachines.biblio.model.Book;

public class BookUnavilableException extends Exception{
    Book book;

    public BookUnavilableException(Book book) {
        this.book = book;
    }

    @Override
    public String getMessage() {
        return String.format("The book %s is unavailable.", book.getTitle());
    }
}
