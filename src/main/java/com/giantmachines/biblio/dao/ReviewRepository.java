package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
