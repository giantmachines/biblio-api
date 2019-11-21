package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Book;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Sql({"classpath:data.sql", "classpath:tests.sql"})
public class ReviewRepositoryTest extends AbstractBaseJpaTest {

    @Autowired
    private ReviewRepository repository;

    @Test
    public void should_return_the_reviews_for_a_specific_book(){
        Book book = this.getEntityManager().find(Book.class, 1L);
        List<Review> reviews = repository.getByBook(book);
        assertEquals(1, reviews.size());
    }

    @Test
    @DirtiesContext
    public void should_return_the_updated_reviews_for_a_specific_book(){
        Book book = this.getEntityManager().find(Book.class, 1L);
        User user = this.getEntityManager().find(User.class, 1L);
        Review review = Review.builder()
                .book(book)
                .reviewer(user)
                .comments("Wow!")
                .build();
        this.getEntityManager().persist(review);
        List<Review> reviews = repository.getByBook(book);
        assertEquals(2, reviews.size());
    }
}
