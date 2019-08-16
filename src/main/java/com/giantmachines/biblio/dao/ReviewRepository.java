package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Query(value = "from Review r where r.reviewer.id = :userId")
    List<Review> getByUserId(@Param("userId") long userId);
}
