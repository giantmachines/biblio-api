package com.giantmachines.biblio.dao;


import com.giantmachines.biblio.testing.Fixtures;
import lombok.Getter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Getter
@DataJpaTest
@EnableJpaAuditing
@TestPropertySource(properties = {
        "spring.datasource.initialization-mode=never"
})
public class AbstractBaseJpaTest {

    private static boolean initialized = false;

    @Autowired
    protected TestEntityManager entityManager;


    @Before
    public void setup() {
        if (!initialized) {
            entityManager.persist(Fixtures.users.get(0));
            Fixtures.authors.forEach(e -> entityManager.persist(e));
            Fixtures.reviews.forEach(e -> entityManager.persist(e));
            Fixtures.books.forEach(e -> entityManager.persist(e));
            entityManager.flush();
            initialized = true;
        }
    }
}
