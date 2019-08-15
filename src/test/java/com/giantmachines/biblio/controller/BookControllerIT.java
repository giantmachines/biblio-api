package com.giantmachines.biblio.controller;

import com.giantmachines.biblio.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql({"classpath:reset.sql"})
public class BookControllerIT {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void should_have_the_correct_path_value(){
        assertEquals("books", context.getBean(BookController.class).getPath());
    }

    /**
     * Metadata is ID, title, author, image
     */
    @Test
    public void should_get_all_active_books_with_metadata_plus_status_and_reviews() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("The Iliad")))
                .andExpect(jsonPath("$[0].author.firstName", is("Homer")))
                .andExpect(jsonPath("$[0].image", is("http://localhost/biblio/books/images/1")))
                .andExpect(jsonPath("$[0].averageRating", is(5.0)))
                .andExpect(jsonPath("$[0].status", is("UNAVAILABLE")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Patterns of Enterprise Software")))
                .andExpect(jsonPath("$[1].author.firstName", is("Martin")))
                .andExpect(jsonPath("$[1].author.lastName", is("Fowler")))
                .andExpect(jsonPath("$[1].image", is("http://localhost/biblio/books/images/2")))
                .andExpect(jsonPath("$[1].averageRating").doesNotExist())
                .andExpect(jsonPath("$[1].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].title", is("Refactoring")))
                .andExpect(jsonPath("$[2].author.firstName", is("Martin")))
                .andExpect(jsonPath("$[2].author.lastName", is("Fowler")))
                .andExpect(jsonPath("$[2].image", is("http://localhost/biblio/books/images/3")))
                .andExpect(jsonPath("$[2].averageRating").doesNotExist())
                .andExpect(jsonPath("$[2].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].title", is("Design Patterns")))
                .andExpect(jsonPath("$[3].author.firstName", is("Eric")))
                .andExpect(jsonPath("$[3].author.lastName", is("Gamma")))
                .andExpect(jsonPath("$[3].image", is("http://localhost/biblio/books/images/4")))
                .andExpect(jsonPath("$[3].averageRating").doesNotExist())
                .andExpect(jsonPath("$[3].status", is("AVAILABLE")));
    }

    @Test
    public void should_get_the_details_with_metadata_for_the_specified_book() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("The Iliad")))
                .andExpect(jsonPath("$.author.firstName", is("Homer")))
                .andExpect(jsonPath("$.image", is("http://localhost/biblio/books/images/1")))
                .andExpect(jsonPath("$.averageRating", is(5.0)))
                .andExpect(jsonPath("$.reviews[0].highlight", is(true)))
                .andExpect(jsonPath("$.reviews[0].rating", is(5)))
                .andExpect(jsonPath("$.reviews[0].reviewer", is("Philip Ford")));
    }

    @Test
    public void shuuld_save_a_new_review() throws Exception{

    }
}
