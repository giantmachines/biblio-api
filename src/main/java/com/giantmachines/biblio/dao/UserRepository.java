package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(@Param("userId") String email);
}
