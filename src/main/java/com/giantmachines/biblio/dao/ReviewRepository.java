package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> getByReviewerId(@Param("userId") long userId);
}
