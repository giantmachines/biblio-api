package com.giantmachines.biblio.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.giantmachines.biblio.model.Review;
import com.giantmachines.biblio.security.CurrentUser;

import java.io.IOException;

public class ReviewSerializer extends JsonSerializer<Review> {
    private CurrentUser currentUser;

    public ReviewSerializer(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void serialize(Review review, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String userName = currentUser.get();
        String reviewer = String.format("%s %s", review.getReviewer().getFirstName(), review.getReviewer().getLastName());
        gen.writeStartObject();
        gen.writeNumberField("id", review.getId());
        gen.writeStringField("reviewer", reviewer);
        gen.writeNumberField("rating", review.getValue());
        gen.writeStringField("comments", review.getComments());
        gen.writeNumberField("createdAt", review.getTimeCreated());  // TODO:  handle client timezone
        gen.writeNumberField("updatedAt", review.getTimeUpdated());
        if (userName != null && review.getReviewer().getEmail().equals(currentUser.get())) {
            gen.writeBooleanField("highlight", true);
        }
        gen.writeEndObject();
    }
}
