package com.giantmachines.biblio.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ReviewSerializer extends JsonSerializer<Review> {

    private final AuditorAware<User> auditService;

    @Override
    public void serialize(Review review, JsonGenerator gen, SerializerProvider provider) throws IOException {
        User currentUser = auditService.getCurrentAuditor().get();
        String userName = currentUser.getEmail();
        String reviewer = String.format("%s %s", review.getReviewer().getFirstName(), review.getReviewer().getLastName());
        gen.writeStartObject();
        gen.writeNumberField("id", review.getId());
        gen.writeStringField("reviewer", reviewer);
        gen.writeNumberField("rating", review.getValue());
        gen.writeStringField("comments", review.getComments());
        gen.writeNumberField("createdAt", review.getTimeCreated());  // TODO:  handle client timezone
        gen.writeNumberField("updatedAt", review.getTimeUpdated());
        if (userName != null && review.getReviewer().getEmail().equals(currentUser.getEmail())) {
            gen.writeBooleanField("highlight", true);
        }
        gen.writeEndObject();
    }
}
