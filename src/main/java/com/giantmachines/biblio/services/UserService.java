package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Provides services for retrieving and updating userrs
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private UserRepository repository;
    private ReviewRepository reviewRepository;

    public UserService(UserRepository repository, ReviewRepository reviewRepository) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
    }

    public User getById(long id){
        return this.repository.findById(id).orElse(null);
    }

    public User getByUserName(String userId){
        return this.repository.findByUserName(userId);
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
     * Set the user as inactive.
     *
     * @param user                  the user to remove from the active list.
     * @throws PersistenceException thrown if an error occurs while saving.
     */
    @Transactional
    public User deactivate(User user) throws PersistenceException{
        User.UserBuilder builder = new User.UserBuilder(user).setActive(false);
        return this.repository.save(new User(builder));
    }

    public List<Review> getUserReviews(long id){
        return this.reviewRepository.getByUserId(id);
    }
}
