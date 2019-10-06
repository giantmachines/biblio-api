package com.giantmachines.biblio.services;

import com.giantmachines.biblio.dao.UserRepository;
import com.giantmachines.biblio.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;

import static org.junit.Assert.*;


public class UserServiceTest extends AbstractBaseServiceTest {

    @Autowired
    UserService service;

    @Autowired
    UserRepository repository;



    @Test
    public void should_return_a_specified_user_by_id() {
        User user = service.getById(1L);
        assertEquals("Philip", user.getFirstName());
        assertEquals("Ford", user.getLastName());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DirtiesContext
    public void should_add_a_new_user() {
        User newUser = User.builder()
                .firstName("John")
                .lastName("Smith")
                .email("jsmith@gmail.com")
                .password("1234")
                .build();
        service.save(newUser);
        Collection<User> users = (Collection) repository.findAll();
        assertEquals(2, users.size());

        User user = repository.findById(2L).get();
        assertEquals("Smith", user.getLastName());
        assertEquals("John", user.getFirstName());
    }

    @Test
    @DirtiesContext
    public void should_update_a_specified_user() {
        User user = repository.findById(1L).get();
        assertTrue(user.isOnline()); // A control
        user = service.setStatus(user, false);
        assertFalse(user.isOnline());
    }

    @Test
    @DirtiesContext
    public void should_deactivate_a_specified_user_account() {
        User user = repository.findById(1L).get();
        assertTrue(user.isActive());  // A control
        user = service.deactivate(user);
        assertFalse(user.isActive());
    }
}
