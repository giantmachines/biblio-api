package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    @Transactional
    public User save(User user) throws PersistenceException {
        return this.repository.save(user);
    }

    @Transactional
    public User setStatus(User user, boolean status){
        User.UserBuilder builder = new User.UserBuilder(user).setOnline(status);
        return repository.save(new User(builder));
    }

    /**
     * Set the user as inactive.  In this way, reviews from former users remain unchanged.
     *
     * @param user The user to remove from the active list.
     * @throws PersistenceException Thrown if an error occurs while saving.
     */
    @Transactional
    public User deactivate(User user) throws PersistenceException{
        User.UserBuilder builder = new User.UserBuilder(user).setActive(false);
        return this.repository.save(new User(builder));
    }
}
