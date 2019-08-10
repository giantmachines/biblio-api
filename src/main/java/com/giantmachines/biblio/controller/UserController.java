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

    @RequestMapping(value = "/{id", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable long id){
        User user = service.getById(id);
        return this.buildOkResponse(user);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody User user){
        return this.buildCreatedResponse(user.getId());
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity logout(@PathVariable long id){
        User user = this.service.getById(id);
        this.service.setStatus(user, false);;
        return this.buildOkResponse(user);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity update(@PathVariable long id){
        User user = this.service.getById(id);
        this.service.deactivate(user);
        return this.buildCreatedResponse(id);
    }
}
