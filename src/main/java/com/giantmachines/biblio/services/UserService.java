package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.ReviewRepository;
import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Provides services for retrieving and updating userrs
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final ReviewRepository reviewRepository;

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
        return repository.save(user.toBuilder().online(status).build());
    }

    /**
     * Set the user as inactive.
     *
     * @param user                  the user to remove from the active list.
     * @throws PersistenceException thrown if an error occurs while saving.
     */
    @Transactional
    public User deactivate(User user) throws PersistenceException{
        return this.repository.save(user.toBuilder().active(false).build());
    }

    public List<Review> getUserReviews(long id){
        return this.reviewRepository.getByUserId(id);
    }
}
