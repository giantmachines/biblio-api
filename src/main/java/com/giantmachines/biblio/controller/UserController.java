package com.giantmachines.biblio.controller;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractBaseController{

    private UserService service;


    public UserController(UserService service) {
        this.service = service;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable long id){
        User user = service.getById(id);
        return this.buildOkResponse(user);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody User user){
        return this.buildCreatedResponse(user.getId());
    }


    /**
     * Performs a "soft delete" on the specified user.  Does not delete the user from the database,
     * for historical reasons.  In this way, reviews from former users remain unchanged.
     *
     * @param id    the user to remove
     * @return      a ResponseEntity
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity softDelete(@PathVariable long id){
        User user = this.service.getById(id);
        this.service.deactivate(user);
        return this.buildCreatedResponse(id);
    }


    @RequestMapping(value = "/{userId}/reviews", method = RequestMethod.GET)
    public ResponseEntity getUserReviews(@PathVariable("userId") long userId){
        return this.buildOkResponse(this.service.getUserReviews(userId));
    }
}
