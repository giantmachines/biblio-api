package com.giantmachines.biblio.serialization;

import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.testing.Fixtures;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class ReviewSerializationTest {

    @Autowired
    private JacksonTester<Review> json;



    @TestConfiguration
    static class SerializationTestConfiguration {

        @Bean
        public AuditorAware<User> auditorProvider() {
            return () -> Optional.ofNullable(User
                    .builder()
                    .id(1L)
                    .email("paford@gmail.com")
                    .build());
        }
    }


    @Test
    public void test_serialize_when_the_user_is_the_reviewer() throws IOException {
        Review review = Fixtures.reviews.get(0).toBuilder().id(1L).build();
        assertThat(json.write(review)).doesNotHaveJsonPathValue("@.value");
        assertThat(json.write(review)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(review)).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(json.write(review)).hasJsonPathNumberValue("@.rating");
        assertThat(json.write(review)).extractingJsonPathNumberValue("@.rating").isEqualTo(5);
        assertThat(json.write(review)).hasJsonPathBooleanValue("@.highlight");
        assertThat(json.write(review)).extractingJsonPathBooleanValue("@.highlight").isTrue();
        assertThat(json.write(review)).hasJsonPathStringValue("@.reviewer");
        assertThat(json.write(review)).extractingJsonPathStringValue("@.reviewer").isEqualTo("Philip Ford");
        assertThat(json.write(review)).hasJsonPathStringValue("@.comments");
    }

    @Test
    public void test_serialize_when_the_user_is_not_the_reviewer() throws IOException {
        User reviewer = User.builder()
                .firstName("John")
                .lastName("Smtih")
                .email("jsmith@gmail.com")
                .build();
        Review review = Review.builder()
                .reviewer(reviewer)
                .value(1)
                .comments("Hated it.")
                .build();
        assertThat(json.write(review)).doesNotHaveJsonPathValue("@.highlight");
    }
}
