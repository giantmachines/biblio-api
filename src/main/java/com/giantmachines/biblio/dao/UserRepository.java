package com.giantmachines.biblio.dao;

import com.giantmachines.biblio.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
