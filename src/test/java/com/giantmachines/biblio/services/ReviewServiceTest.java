package com.giantmachines.biblio.services;


import com.giantmachines.biblio.Application;
import com.giantmachines.biblio.model.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Sql({"classpath:reset.sql"})
public class ReviewServiceTest {

    @Autowired
    ReviewService service;

    @Autowired
    UserService userService;

    @Test
    public void should_return_the_specified_review(){
        Review review = service.getById(1L);
        assertEquals(review.getValue(), 5);
        assertEquals(review.getReviewer().getEmail(), "paford@gmail.com");
    }

    @Test
    public void should_update_the_comments_for_the_specified_review(){
        String newComment = "It was OK.";
        Review review = service.getById(1L);
        Review update = review.toBuilder()
                .reviewer(review.getReviewer())
                .value(5)
                .comments(newComment)
                .build();
        review = service.update(update);
        assertEquals(newComment, review.getComments());
    }

    @Test
    public void should_update_the_rating_for_the_specified_review(){
        int newRating = 4;
        Review review = service.getById(1L);
        Review update = review.toBuilder().id(1L).value(4).build();
        review = service.update(update);
        assertEquals(newRating, review.getValue());
    }

}
