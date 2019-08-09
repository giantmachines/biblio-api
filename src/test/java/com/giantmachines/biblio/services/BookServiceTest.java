package com.giantmachines.biblio.services;

import com.giantmachines.biblio.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@ActiveProfiles("Test")
public class BookServiceTest {

    @Test
    public void should_return_the_correct_book_for_the_requested_id(){

    }
}
