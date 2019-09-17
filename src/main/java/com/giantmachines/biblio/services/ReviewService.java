package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.model.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public Review getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    @Transactional
    public Review update(Review review) {
        Review current = repository.findById(review.getId()).orElse(null);
        if (current == null) {
            throw new IllegalArgumentException("Cannot save now reviews");
        }

        Review result = current.toBuilder()
                .value(review.getValue())
                .comments(review.getComments())
                .build();
        return this.repository.save(result);
    }
}
