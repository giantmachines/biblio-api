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
                .andExpect(jsonPath("$[0].rating", is(5.0)))
                .andExpect(jsonPath("$[0].status", is("UNAVAILABLE")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Patterns of Enterprise Software")))
                .andExpect(jsonPath("$[1].author.firstName", is("Martin")))
                .andExpect(jsonPath("$[1].author.lastName", is("Fowler")))
                .andExpect(jsonPath("$[1].image", is("http://localhost/biblio/books/images/2")))
                .andExpect(jsonPath("$[1].rating", is(-1.0)))
                .andExpect(jsonPath("$[1].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].title", is("Refactoring")))
                .andExpect(jsonPath("$[2].author.firstName", is("Martin")))
                .andExpect(jsonPath("$[2].author.lastName", is("Fowler")))
                .andExpect(jsonPath("$[2].image", is("http://localhost/biblio/books/images/3")))
                .andExpect(jsonPath("$[2].rating", is(-1.0)))
                .andExpect(jsonPath("$[2].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].title", is("Design Patterns")))
                .andExpect(jsonPath("$[3].author.firstName", is("Eric")))
                .andExpect(jsonPath("$[3].author.lastName", is("Gamma")))
                .andExpect(jsonPath("$[3].image", is("http://localhost/biblio/books/images/4")))
                .andExpect(jsonPath("$[3].rating", is(-1.0)))
                .andExpect(jsonPath("$[3].status", is("AVAILABLE")));
    }
}
