package com.giantmachines.biblio.testing;

import com.giantmachines.biblio.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fixtures {
    public static List<User> users = new ArrayList<>();
    static {
        users.add(User.builder()
                .firstName("Philip")
                .lastName("Ford")
                .active(true)
                .online(true)
                .email("paford@gmail.com")
                .password("{noop}1234")
                .build());
    }

    public static List<Author> authors = new ArrayList<>();
    static {
        authors.add(Author.builder()
                .firstName("Homer")
                .build());
        authors.add(Author.builder()
                .firstName("Martin")
                .lastName("Fowler")
                .build());
        authors.add(Author.builder()
                .firstName("Eric")
                .lastName("Gamma")
                .build());
        authors.add(Author.builder()
                .firstName("Harold")
                .lastName("Camping")
                .build());
    }

    public static List<Review> reviews = new ArrayList<>();
    static {
        reviews.add(Review.builder()
                .value(5)
                .reviewer(users.get(0))
                .comments("I liked it.")
                .build());
    }

    public static List<Book> books = new ArrayList<>();
    static {
        books.add(Book.builder()
                .title("The Iliad")
                .author(authors.get(0))
                .image("http://localhost/biblio/books/images/1")
                .status(Status.UNAVAILABLE)
                //.reviews(Collections.singletonList(reviews.get(0)))
                .build());
        books.add(Book.builder()
                .title("Patterns of Enterprise Software")
                .author(authors.get(1))
                .image("http://localhost/biblio/books/images/2")
                .build());
        books.add(Book.builder()
                .title("Refactoring")
                .author(authors.get(1))
                .image("http://localhost/biblio/books/images/3")
                .build());
        books.add(Book.builder()
                .title("Design Patterns")
                .author(authors.get(2))
                .image("http://localhost/biblio/books/images/4")
                .build());
        books.add(Book.builder()
                .title("!994")
                .author(authors.get(3))
                .image("http://localhost/biblio/books/images/5")
                .status(Status.DEACTIVATED)
                .build());
    }
}
