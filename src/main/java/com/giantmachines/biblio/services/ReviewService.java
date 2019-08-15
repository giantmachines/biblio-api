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

    public Review getRById(long id){
        return this.repository.findById(id).orElse(null);
    }

    @Transactional
    public Review save(Review review){
        return this.repository.save(review);
    }

    @Transactional
    public void delete(long id){
        Review review = this.repository.findById(id).orElse(null);
        if (review != null) {
            this.repository.delete(review);
        }
    }
}
