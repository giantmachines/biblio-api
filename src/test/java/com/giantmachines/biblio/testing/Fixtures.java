package com.giantmachines.biblio.testing;

import com.giantmachines.biblio.model.*;

import java.util.*;

public class Fixtures {
    public static List<User> users = new ArrayList<>();

    static {
        users.add(User.builder()
                .id(1L)
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
                .id(1L)
                .firstName("Homer")
                .build());
        authors.add(Author.builder()
                .id(2L)
                .firstName("Martin")
                .lastName("Fowler")
                .build());
        authors.add(Author.builder()
                .id(3L)
                .firstName("Eric")
                .lastName("Gamma")
                .build());
        authors.add(Author.builder()
                .id(4L)
                .firstName("Harold")
                .lastName("Camping")
                .build());
    }

    public static List<Review> reviews = new ArrayList<>();

    static {
        reviews.add(Review.builder()
                .id(1L)
                .value(5)
                .reviewer(users.get(0))
                .comments("I liked it.")
                .build());
    }

    public static List<Book> books = new ArrayList<>();

    static {
        books.add(Book.builder()
                .id(1L)
                .title("The Iliad")
                .author(authors.get(0))
                .image("http://localhost/biblio/books/images/1")
                .status(Status.UNAVAILABLE)
                .reviews(Collections.singletonList(reviews.get(0)))
                .build());
        books.add(Book.builder()
                .id(2L)
                .title("Patterns of Enterprise Software")
                .author(authors.get(1))
                .image("http://localhost/biblio/books/images/2")
                .status(Status.AVAILABLE)
                .build());
        books.add(Book.builder()
                .id(3L)
                .title("Refactoring")
                .author(authors.get(1))
                .image("http://localhost/biblio/books/images/3")
                .status(Status.AVAILABLE)
                .build());
        books.add(Book.builder()
                .id(4L)
                .title("Design Patterns")
                .author(authors.get(2))
                .image("http://localhost/biblio/books/images/4")
                .status(Status.AVAILABLE)
                .build());
        books.add(Book.builder()
                .id(5L)
                .title("!994")
                .author(authors.get(3))
                .image("http://localhost/biblio/books/images/5")
                .status(Status.DEACTIVATED)
                .build());
    }
}
