package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "from User u where u.email = :userId")
    User findByUserName(@Param("userId") String userName);
}
